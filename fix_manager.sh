#!/bin/bash
awk '/suspend fun loadPrivateKeyBytes/{
    system("cat patch_file.kt")
    print "    suspend fun loadPrivateKeyBytes(keyId: Long): ByteArray = withContext(Dispatchers.IO) {"
    print "        val keysDir = File(context.filesDir, \".ssh\")"
    print "        val privFile = File(keysDir, \"id_${keyId}\")"
    print "        val encryptedBytes = privFile.readBytes()"
    print "        decryptPrivateKey(encryptedBytes)"
    print "    }"
    print ""
    print "    suspend fun loadPublicKeyString(keyId: Long): String? = withContext(Dispatchers.IO) {"
    print "        val keysDir = File(context.filesDir, \".ssh\")"
    print "        val pubFile = File(keysDir, \"id_${keyId}.pub\")"
    print "        if (pubFile.exists()) pubFile.readText(Charsets.UTF_8) else null"
    print "    }"
    print ""
    print "    suspend fun deleteKeyFiles(keyId: Long) = withContext(Dispatchers.IO) {"
    print "        val keysDir = File(context.filesDir, \".ssh\")"
    print "        File(keysDir, \"id_${keyId}\").delete()"
    print "        File(keysDir, \"id_${keyId}.pub\").delete()"
    print "        val db = RouterDatabase.getDatabase(context)"
    print "        val key = db.sshKeyDao().getKeyById(keyId)"
    print "        if (key != null) {"
    print "            db.sshKeyDao().deleteKey(key)"
    print "        }"
    print "    }"
    print "}"
    exit 0
} {print}' temp.kt > app/src/main/java/com/example/data/SshKeyManager.kt
