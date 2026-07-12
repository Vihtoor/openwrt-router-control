import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

content = content.replace('val connectionType = if (dev.startsWith("eth") || dev.startsWith("lan")) "Ethernet" else "LAN"', 'val connectionType = "Ethernet"')
content = content.replace('devices.add(DeviceTraffic(mac, ip, hostname, "LAN", 0L, 0L))', 'devices.add(DeviceTraffic(mac, ip, hostname, "Ethernet", 0L, 0L))')

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)
