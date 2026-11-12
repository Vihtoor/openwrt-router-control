import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

content = content.replace("class SshClientManager {", "import android.content.Context\n\nclass SshClientManager(private val context: Context) {")

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
