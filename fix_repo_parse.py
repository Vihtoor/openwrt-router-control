import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

# Update call to parseDevices
old_call = "val devices = parseDevices(wifiPart, leasesPart, arpPart)"
new_call = "val devices = parseDevices(wifiPart, leasesPart, arpPart, config.ipAddress)"
content = content.replace(old_call, new_call)

# Update parseDevices function
old_func_sig = "private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String): List<DeviceTraffic> {"
new_func_sig = "private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String, routerIp: String): List<DeviceTraffic> {"
content = content.replace(old_func_sig, new_func_sig)

# We will modify the Wi-Fi loop inside parseDevices
# Let's find the Wi-Fi loop
old_wifi_loop = """        // Parse Wi-Fi: mac rx tx iface
        for (line in wifiPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 4) {
                val mac = parts[0].lowercase()
                val rx = parts[1].toLongOrNull() ?: 0L
                val tx = parts[2].toLongOrNull() ?: 0L
                val iface = parts[3]
                val ip = macToIp[mac] ?: "Unknown"
                val hostname = macToHostname[mac] ?: "Unknown"
                devices.add(DeviceTraffic(mac, ip, hostname, "Wi-Fi ($iface)", rx, tx))
                seenMacs.add(mac)
            }
        }"""

new_wifi_loop = """        // Parse Wi-Fi: mac rx tx iface freq
        for (line in wifiPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 4) {
                val mac = parts[0].lowercase()
                val rx = parts[1].toLongOrNull() ?: 0L
                val tx = parts[2].toLongOrNull() ?: 0L
                val iface = parts[3]
                var freqStr = if (parts.size >= 5) parts[4] else "0"
                val freq = freqStr.toIntOrNull() ?: 0
                
                var band = "Wi-Fi ($iface)"
                if (freq in 2400..2500) band = "Wi-Fi 2.4 ГГц"
                else if (freq in 5000..5900) band = "Wi-Fi 5 ГГц"
                else if (freq in 5900..7200) band = "Wi-Fi 6 ГГц"
                
                val ip = macToIp[mac] ?: "Unknown"
                if (ip != routerIp && ip != "127.0.0.1") {
                    val hostname = macToHostname[mac] ?: "Unknown"
                    devices.add(DeviceTraffic(mac, ip, hostname, band, rx, tx))
                    seenMacs.add(mac)
                }
            }
        }"""
content = content.replace(old_wifi_loop, new_wifi_loop)

# Let's modify the LAN loop to also filter IP
old_lan_loop = """        // Add remaining from ARP/Leases as LAN (no byte counters available)
        for ((mac, ip) in macToIp) {
            if (!seenMacs.contains(mac)) {
                val hostname = macToHostname[mac] ?: "Unknown"
                devices.add(DeviceTraffic(mac, ip, hostname, "LAN", 0L, 0L))
            }
        }"""

new_lan_loop = """        // Add remaining from ARP/Leases as LAN (no byte counters available)
        for ((mac, ip) in macToIp) {
            if (!seenMacs.contains(mac) && ip != routerIp && ip != "127.0.0.1") {
                val hostname = macToHostname[mac] ?: "Unknown"
                devices.add(DeviceTraffic(mac, ip, hostname, "LAN", 0L, 0L))
            }
        }"""
content = content.replace(old_lan_loop, new_lan_loop)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)

