import re

with open("app/src/main/java/com/example/RouterWidgetProvider.kt", "r") as f:
    content = f.read()

content = content.replace("private val sharedSsh = SshClientManager()", "private var sharedSsh: SshClientManager? = null\n        private fun getSharedSsh(context: android.content.Context): SshClientManager {\n            if (sharedSsh == null) sharedSsh = SshClientManager(context.applicationContext)\n            return sharedSsh!!\n        }")
content = content.replace("sharedSsh", "getSharedSsh(context)")

with open("app/src/main/java/com/example/RouterWidgetProvider.kt", "w") as f:
    f.write(content)
