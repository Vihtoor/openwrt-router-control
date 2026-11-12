with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

content = content.replace('properties["StrictHostKeyChecking"] = "no"', 'properties["StrictHostKeyChecking"] = "no"\n            properties["PubkeyAcceptedAlgorithms"] = "ssh-ed25519,rsa-sha2-512,rsa-sha2-256,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521"\n            properties["server_host_key"] = "ssh-ed25519,rsa-sha2-512,rsa-sha2-256,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521"\n')

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
