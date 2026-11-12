with open("app/src/main/java/com/example/data/SshKeyManager.kt", "r") as f:
    content = f.read()

content = content.replace('val keySize = if (algorithm == KeyPair.RSA) 2048 else 256', 'val keySize = if (algorithm == KeyPair.RSA) 3072 else 256')

with open("app/src/main/java/com/example/data/SshKeyManager.kt", "w") as f:
    f.write(content)
