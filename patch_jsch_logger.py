import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

replacement = """    init {
        com.jcraft.jsch.JSch.setLogger(object : com.jcraft.jsch.Logger {
            override fun isEnabled(level: Int) = true
            override fun log(level: Int, message: String) {
                android.util.Log.d("JSch-Internal", message)
            }
        })
        try {"""

content = content.replace("    init {\n        try {", replacement)

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)

