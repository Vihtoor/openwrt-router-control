import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "onTest(ip, p, user, password, sshKeyId)",
    "android.util.Log.d(\"SettingsPanel\", \"Building config for test: sshKeyId=$sshKeyId\")\n                                onTest(ip, p, user, password, sshKeyId)"
)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w") as f:
    f.write(content)
