import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_fun = """    private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String, routerIp: String): List<DeviceTraffic> {"""

new_fun = """    private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String, nlbwPart: String, routerIp: String): List<DeviceTraffic> {
        val nlbwRx = mutableMapOf<String, Long>()
        val nlbwTx = mutableMapOf<String, Long>()
        for (line in nlbwPart.lines()) {
            val parts = line.trim().split(",")
            if (parts.size >= 4) {
                val mac = parts[0].replace('"', '').lowercase()
                val rx = parts[2].replace('"', '').toLongOrNull() ?: 0L
                val tx = parts[3].replace('"', '').toLongOrNull() ?: 0L
                nlbwRx[mac] = rx
                nlbwTx[mac] = tx
            }
        }"""

content = content.replace(old_fun, new_fun)

old_wifi_add = """                    devices.add(DeviceTraffic(mac, ip, hostname, band, rx, tx))"""
new_wifi_add = """                    val monthRx = nlbwRx[mac] ?: 0L
                    val monthTx = nlbwTx[mac] ?: 0L
                    devices.add(DeviceTraffic(mac, ip, hostname, band, rx, tx, monthRx, monthTx))"""
content = content.replace(old_wifi_add, new_wifi_add)

old_lan_add = """                devices.add(DeviceTraffic(mac, ip, hostname, connectionType, 0L, 0L))"""
new_lan_add = """                val monthRx = nlbwRx[mac] ?: 0L
                val monthTx = nlbwTx[mac] ?: 0L
                devices.add(DeviceTraffic(mac, ip, hostname, connectionType, 0L, 0L, monthRx, monthTx))"""
content = content.replace(old_lan_add, new_lan_add)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)

print("parseDevices updated")
