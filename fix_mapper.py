import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

old_mapper = """                DeviceSpeedInfo(
                    mac = device.mac,
                    ip = device.ip,
                    hostname = device.hostname,
                    connectionType = device.connectionType,
                    downloadSpeedMbps = dlSpeed,
                    uploadSpeedMbps = ulSpeed,
                    downloadBytes = device.txBytes, // From router's perspective, TX to device is device's download
                    uploadBytes = device.rxBytes  // From router's perspective, RX from device is device's upload
                )"""

new_mapper = """                DeviceSpeedInfo(
                    mac = device.mac,
                    ip = device.ip,
                    hostname = device.hostname,
                    connectionType = device.connectionType,
                    downloadSpeedMbps = dlSpeed,
                    uploadSpeedMbps = ulSpeed,
                    downloadBytes = device.txBytes, // From router's perspective, TX to device is device's download
                    uploadBytes = device.rxBytes,  // From router's perspective, RX from device is device's upload
                    downloadMonthBytes = device.txMonthBytes,
                    uploadMonthBytes = device.rxMonthBytes
                )"""

if old_mapper in content:
    content = content.replace(old_mapper, new_mapper)
    with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
        f.write(content)
    print("Mapper updated")
else:
    print("Mapper not found")
