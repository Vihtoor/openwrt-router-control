import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_traffic = """data class DeviceTraffic(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val rxBytes: Long,
    val txBytes: Long
)"""

new_traffic = """data class DeviceTraffic(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val rxBytes: Long,
    val txBytes: Long,
    val rxMonthBytes: Long = 0L,
    val txMonthBytes: Long = 0L
)"""

if old_traffic in content:
    content = content.replace(old_traffic, new_traffic)
    with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
        f.write(content)
    print("DeviceTraffic updated")
else:
    print("DeviceTraffic not found")

