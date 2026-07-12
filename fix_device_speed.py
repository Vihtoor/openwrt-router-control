import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

old_data_class = """data class DeviceSpeedInfo(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float
)"""

new_data_class = """data class DeviceSpeedInfo(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float,
    val downloadBytes: Long = 0L,
    val uploadBytes: Long = 0L
)"""

content = content.replace(old_data_class, new_data_class)

old_result_add = """            result.add(
                DeviceSpeedInfo(
                    mac = device.mac,
                    ip = device.ip,
                    hostname = device.hostname,
                    connectionType = device.connectionType,
                    downloadSpeedMbps = dlSpeed,
                    uploadSpeedMbps = ulSpeed
                )
            )"""

new_result_add = """            result.add(
                DeviceSpeedInfo(
                    mac = device.mac,
                    ip = device.ip,
                    hostname = device.hostname,
                    connectionType = device.connectionType,
                    downloadSpeedMbps = dlSpeed,
                    uploadSpeedMbps = ulSpeed,
                    downloadBytes = device.txBytes, // From router's perspective, TX to device is device's download
                    uploadBytes = device.rxBytes  // From router's perspective, RX from device is device's upload
                )
            )"""

content = content.replace(old_result_add, new_result_add)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
