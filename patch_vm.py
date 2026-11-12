import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "fun testConnection(ip: String, port: Int, user: String, pass: String, sshKeyId: Long? = null) {\n        viewModelScope.launch {",
    "fun testConnection(ip: String, port: Int, user: String, pass: String, sshKeyId: Long? = null) {\n        android.util.Log.d(\"RouterViewModel\", \"testConnection called with sshKeyId=$sshKeyId\")\n        viewModelScope.launch {"
)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
