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
