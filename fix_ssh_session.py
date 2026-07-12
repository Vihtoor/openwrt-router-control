import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

# Replace the equality check in getConnectedSession
content = re.sub(
    r'if \(currentSession != null && currentSession\.isConnected && lastConfig == config\) \{',
    r'if (currentSession != null && currentSession.isConnected && lastConfig?.id == config.id && lastConfig?.ipAddress == config.ipAddress && lastConfig?.port == config.port && lastConfig?.username == config.username && lastConfig?.sshKeyOrPassword == config.sshKeyOrPassword) {',
    content
)

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
