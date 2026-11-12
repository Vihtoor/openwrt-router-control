with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

target = """if (currentSession != null && currentSession.isConnected && lastConfig?.id == config.id && lastConfig?.ipAddress == config.ipAddress && lastConfig?.port == config.port && lastConfig?.username == config.username && lastConfig?.sshKeyOrPassword == config.sshKeyOrPassword)"""
replacement = """if (currentSession != null && currentSession.isConnected && lastConfig?.id == config.id && lastConfig?.ipAddress == config.ipAddress && lastConfig?.port == config.port && lastConfig?.username == config.username && lastConfig?.sshKeyOrPassword == config.sshKeyOrPassword && lastConfig?.sshKeyId == config.sshKeyId)"""

if target in content:
    content = content.replace(target, replacement)
    print("Patched SshClientManager reuse check")
else:
    print("Target reuse not found")

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
