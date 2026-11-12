with open("app/src/main/java/com/example/data/RouterConfig.kt", "r") as f:
    content = f.read()

content = content.replace("val sshKeyOrPassword: String,", "val sshKeyOrPassword: String,\n    val sshKeyId: Long? = null,")

with open("app/src/main/java/com/example/data/RouterConfig.kt", "w") as f:
    f.write(content)
