import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

# Add Log.e("SSH-Debug", "Caught exception in testConnection", e)
content = re.sub(
    r'(catch \(e: Exception\) \{\n\s*)(Log\.e\("SshClientManager", "Connection test failed:[^\n]+)',
    r'\1Log.e("SSH-Debug", "Caught exception in SshClientManager.testConnection", e)\n            \2',
    content
)

# Also patch startInteractiveShell
content = re.sub(
    r'(catch \(e: Exception\) \{\n\s*)(Log\.e\("SshClientManager", "Interactive shell read error:[^\n]+)',
    r'\1Log.e("SSH-Debug", "Caught exception in SshClientManager interactive shell read", e)\n                                \2',
    content
)

content = re.sub(
    r'(catch \(e: Exception\) \{\n\s*)(Log\.e\("SshClientManager", "Failed to start interactive shell:[^\n]+)',
    r'\1Log.e("SSH-Debug", "Caught exception in SshClientManager.startInteractiveShell", e)\n                \2',
    content
)

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)

