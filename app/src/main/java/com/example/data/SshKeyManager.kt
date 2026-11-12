package com.example.data

import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SshKeyManager(private val context: Context) {
    private val keyStoreAlias = "ssh_master_key_v1"
    private val androidKeyStore = "AndroidKeyStore"
    private val transformation = "AES/GCM/NoPadding"

    init {
        try {
            if (Security.getProvider("BC") !is BouncyCastleProvider) {
                Security.removeProvider("BC")
                Security.insertProviderAt(BouncyCastleProvider(), 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initKeystore()
    }

    private fun initKeystore() {
        val keyStore = KeyStore.getInstance(androidKeyStore)
        keyStore.load(null)
        if (!keyStore.containsAlias(keyStoreAlias)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)
            val spec = KeyGenParameterSpec.Builder(
                keyStoreAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(androidKeyStore)
        keyStore.load(null)
        return keyStore.getKey(keyStoreAlias, null) as SecretKey
    }

    private fun encryptPrivateKey(plainBytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plainBytes)
        val out = ByteArrayOutputStream()
        out.write(iv.size)
        out.write(iv)
        out.write(encrypted)
        return out.toByteArray()
    }

    private fun decryptPrivateKey(encryptedBytes: ByteArray): ByteArray {
        val ivSize = encryptedBytes[0].toInt()
        val iv = encryptedBytes.copyOfRange(1, 1 + ivSize)
        val encryptedData = encryptedBytes.copyOfRange(1 + ivSize, encryptedBytes.size)

        val cipher = Cipher.getInstance(transformation)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return cipher.doFinal(encryptedData)
    }

    suspend fun generateAndSaveKey(
        alias: String,
        algorithm: Int, // KeyPair.ED25519 or KeyPair.RSA
        passphrase: String?,
        comment: String = "user@android"
    ): SshKeyEntity = withContext(Dispatchers.IO) {
        val jsch = JSch()
        
        val privBytes: ByteArray
        val pubBytes: ByteArray
        val fingerprint: String

        if (algorithm == KeyPair.ED25519) {
            if (!passphrase.isNullOrEmpty()) {
                throw UnsupportedOperationException("Библиотека JSch не поддерживает создание ED25519 ключей с паролем. Оставьте поле пароля пустым или используйте RSA.")
            }
            
            val gen = org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator()
            gen.init(org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters(java.security.SecureRandom()))
            val kp = gen.generateKeyPair()
            val encoded = org.bouncycastle.crypto.util.OpenSSHPrivateKeyUtil.encodePrivateKey(kp.private)
            
            val base64Key = android.util.Base64.encodeToString(encoded, android.util.Base64.NO_WRAP)
            val privString = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
                             base64Key.chunked(70).joinToString("\n") + "\n" +
                             "-----END OPENSSH PRIVATE KEY-----\n"
            privBytes = privString.toByteArray(Charsets.UTF_8)
            
            val tempKp = KeyPair.load(jsch, privBytes, null)
            val pubOut = ByteArrayOutputStream()
            tempKp.writePublicKey(pubOut, comment)
            pubBytes = pubOut.toByteArray()
            fingerprint = tempKp.fingerPrint
            tempKp.dispose()
        } else {
            val keySize = if (algorithm == KeyPair.RSA) 3072 else 256
            val keyPair = KeyPair.genKeyPair(jsch, algorithm, keySize)
            
            val privOut = ByteArrayOutputStream()
            keyPair.writePrivateKey(privOut, passphrase?.toByteArray(Charsets.UTF_8))
            privBytes = privOut.toByteArray()
            
            val pubOut = ByteArrayOutputStream()
            keyPair.writePublicKey(pubOut, comment)
            pubBytes = pubOut.toByteArray()
            fingerprint = keyPair.fingerPrint
            
            keyPair.dispose()
        }

        val encryptedPrivBytes = encryptPrivateKey(privBytes)
        privBytes.fill(0) // Zeroing memory

        val keysDir = File(context.filesDir, ".ssh")
        if (!keysDir.exists()) keysDir.mkdirs()

        val tempId = System.currentTimeMillis()
        val privFile = File(keysDir, "id_${tempId}")
        val pubFile = File(keysDir, "id_${tempId}.pub")

        privFile.writeBytes(encryptedPrivBytes)
        pubFile.writeBytes(pubBytes)

        val entity = SshKeyEntity(
            alias = alias,
            algorithm = if (algorithm == KeyPair.ED25519) "ED25519" else "RSA",
            fingerprintSha256 = fingerprint,
            createdAt = System.currentTimeMillis()
        )

        val db = RouterDatabase.getDatabase(context)
        val insertedId = db.sshKeyDao().insertKey(entity)
        
        val realPrivFile = File(keysDir, "id_${insertedId}")
        val realPubFile = File(keysDir, "id_${insertedId}.pub")
        privFile.renameTo(realPrivFile)
        pubFile.renameTo(realPubFile)

        db.sshKeyDao().getKeyById(insertedId)!!
    }

    suspend fun importAndSaveKey(
        alias: String,
        privateKeyBytes: ByteArray,
        publicKeyString: String?,
        passphrase: String?
    ): SshKeyEntity = withContext(Dispatchers.IO) {
        val jsch = JSch()
        val kp = KeyPair.load(jsch, privateKeyBytes, null)
        
        val algorithmName = when (kp.keyType) {
            KeyPair.RSA -> "RSA"
            KeyPair.DSA -> "DSA"
            KeyPair.ECDSA -> "ECDSA"
            KeyPair.ED25519 -> "ED25519"
            else -> "UNKNOWN"
        }
        
        val fingerprint = kp.fingerPrint
        
        val finalPubBytes = if (!publicKeyString.isNullOrBlank()) {
            publicKeyString.toByteArray(Charsets.UTF_8)
        } else {
            if (kp.isEncrypted) {
                if (passphrase != null && kp.decrypt(passphrase)) {
                    val pubOut = ByteArrayOutputStream()
                    kp.writePublicKey(pubOut, alias)
                    pubOut.toByteArray()
                } else {
                    throw Exception("Невозможно извлечь публичный ключ: неверный пароль")
                }
            } else {
                val pubOut = ByteArrayOutputStream()
                kp.writePublicKey(pubOut, alias)
                pubOut.toByteArray()
            }
        }
        
        kp.dispose()
        
        val encryptedPrivBytes = encryptPrivateKey(privateKeyBytes)
        
        val keysDir = File(context.filesDir, ".ssh")
        if (!keysDir.exists()) keysDir.mkdirs()

        val tempId = System.currentTimeMillis()
        val privFile = File(keysDir, "id_${tempId}")
        val pubFile = File(keysDir, "id_${tempId}.pub")

        privFile.writeBytes(encryptedPrivBytes)
        pubFile.writeBytes(finalPubBytes)

        val entity = SshKeyEntity(
            alias = alias,
            algorithm = algorithmName,
            fingerprintSha256 = fingerprint,
            createdAt = System.currentTimeMillis()
        )

        val db = RouterDatabase.getDatabase(context)
        val insertedId = db.sshKeyDao().insertKey(entity)
        
        val realPrivFile = File(keysDir, "id_${insertedId}")
        val realPubFile = File(keysDir, "id_${insertedId}.pub")
        privFile.renameTo(realPrivFile)
        pubFile.renameTo(realPubFile)

        db.sshKeyDao().getKeyById(insertedId)!!
    }

    suspend fun loadPrivateKeyBytes(keyId: Long): ByteArray = withContext(Dispatchers.IO) {
        val keysDir = File(context.filesDir, ".ssh")
        val privFile = File(keysDir, "id_${keyId}")
        val encryptedBytes = privFile.readBytes()
        decryptPrivateKey(encryptedBytes)
    }

    suspend fun loadPublicKeyString(keyId: Long): String? = withContext(Dispatchers.IO) {
        val keysDir = File(context.filesDir, ".ssh")
        val pubFile = File(keysDir, "id_${keyId}.pub")
        if (pubFile.exists()) pubFile.readText(Charsets.UTF_8) else null
    }

    suspend fun deleteKeyFiles(keyId: Long) = withContext(Dispatchers.IO) {
        val keysDir = File(context.filesDir, ".ssh")
        File(keysDir, "id_${keyId}").delete()
        File(keysDir, "id_${keyId}.pub").delete()
        val db = RouterDatabase.getDatabase(context)
        val key = db.sshKeyDao().getKeyById(keyId)
        if (key != null) {
            db.sshKeyDao().deleteKey(key)
        }
    }
}
