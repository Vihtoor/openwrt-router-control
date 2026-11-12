import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

# Fix addIdentity to use pubBytes
content = content.replace('jsch.addIdentity("key_${config.sshKeyId}", privBytes, null, passphraseBytes)', 'jsch.addIdentity("key_${config.sshKeyId}", privBytes, pubBytes, passphraseBytes)')

# Add PreferredAuthentications
target_props = 'properties["StrictHostKeyChecking"] = "no"'
replacement_props = '''properties["StrictHostKeyChecking"] = "no"
            if (config.sshKeyId != null) {
                properties["PreferredAuthentications"] = "publickey"
            } else {
                properties["PreferredAuthentications"] = "password,keyboard-interactive"
            }'''
content = content.replace(target_props, replacement_props)

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
