with open("app/src/main/java/com/example/data/RouterConfig.kt", "r") as f:
    content = f.read()

replacement = """    val hasWiredFdbSupport: Boolean = true,
    val temperatureSource: String = "UNKNOWN",
    val temperaturePaths: String = "",
    val lastCheckedTimestamp: Long = 0L"""

content = content.replace("""    val hasWiredFdbSupport: Boolean = true,
    val lastCheckedTimestamp: Long = 0L""", replacement)

with open("app/src/main/java/com/example/data/RouterConfig.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/data/RouterDatabase.kt", "r") as f:
    content = f.read()

content = content.replace("version = 8", "version = 9")

with open("app/src/main/java/com/example/data/RouterDatabase.kt", "w") as f:
    f.write(content)

