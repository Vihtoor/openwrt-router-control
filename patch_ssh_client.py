import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

target = """            val jsch = JSch()
            val cleanIp = config.ipAddress.removePrefix("[").removeSuffix("]")
            val session = jsch.getSession(config.username, cleanIp, config.port)
            session.setPassword(config.sshKeyOrPassword)"""

replacement = """            val jsch = JSch()
            val cleanIp = config.ipAddress.removePrefix("[").removeSuffix("]")
            
            var privBytes: ByteArray? = null
            var pubBytes: ByteArray? = null
            var passphraseBytes: ByteArray? = null
            
            if (config.sshKeyId != null) {
                val keyManager = SshKeyManager(context)
                privBytes = keyManager.loadPrivateKeyBytes(config.sshKeyId)
                pubBytes = keyManager.loadPublicKeyString(config.sshKeyId)?.toByteArray(Charsets.UTF_8)
                if (config.sshKeyOrPassword.isNotEmpty()) {
                    passphraseBytes = config.sshKeyOrPassword.toByteArray(Charsets.UTF_8)
                }
                jsch.addIdentity("key_${config.sshKeyId}", privBytes, pubBytes, passphraseBytes)
            }

            val session = jsch.getSession(config.username, cleanIp, config.port)
            if (config.sshKeyId == null) {
                session.setPassword(config.sshKeyOrPassword)
            }"""

if target in content:
    content = content.replace(target, replacement)
    
    # Let's also add the wiping of privBytes after connect.
    target_connect = """            session.serverAliveCountMax = 3 // disconnect after 3 missed keepalives
            session.connect()
            activeSession = session"""
    
    replacement_connect = """            session.serverAliveCountMax = 3 // disconnect after 3 missed keepalives
            try {
                session.connect()
            } finally {
                // Zeroing memory of private key bytes immediately after handshake
                privBytes?.fill(0)
                passphraseBytes?.fill(0)
            }
            activeSession = session"""
            
    content = content.replace(target_connect, replacement_connect)
    print("Patched SshClientManager")
else:
    print("Target not found in SshClientManager")

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
