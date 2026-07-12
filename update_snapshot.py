with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

replacement = """data class SpeedSnapshot(
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float,
    val cpuUsagePercent: Float = 0f,
    val cpuTemperature: Float? = null,
    val timestamp: Long = System.currentTimeMillis()
)"""

content = content.replace("""data class SpeedSnapshot(
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float,
    val cpuUsagePercent: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)""", replacement)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
