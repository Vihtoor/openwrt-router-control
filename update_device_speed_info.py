import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

old_info = """data class DeviceSpeedInfo(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float,
    val downloadBytes: Long = 0L,
    val uploadBytes: Long = 0L
)"""

new_info = """data class DeviceSpeedInfo(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float,
    val downloadBytes: Long = 0L,
    val uploadBytes: Long = 0L,
    val downloadMonthBytes: Long = 0L,
    val uploadMonthBytes: Long = 0L
)"""

if old_info in content:
    content = content.replace(old_info, new_info)
    with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
        f.write(content)
    print("DeviceSpeedInfo updated")
else:
    print("DeviceSpeedInfo not found")

