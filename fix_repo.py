import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_parse_devices = """    private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String, routerIp: String): List<DeviceTraffic> {
        val macToHostname = mutableMapOf<String, String>()
        val macToIp = mutableMapOf<String, String>()
        
        // Parse Leases: timestamp mac ip hostname clientid
        for (line in leasesPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 4) {
                val mac = parts[1].lowercase()
                val ip = parts[2]
                val hostname = parts[3].takeIf { it != "*" } ?: "Unknown"
                macToHostname[mac] = hostname
                macToIp[mac] = ip
            }
        }
        
        // Parse ARP: IP HW type Flags HW address Mask Device
        for (line in arpPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 4 && parts[3] != "00:00:00:00:00:00") {
                val ip = parts[0]
                val mac = parts[3].lowercase()
                if (!macToIp.containsKey(mac)) {
                    macToIp[mac] = ip
                }
            }
        }
        
        val devices = mutableListOf<DeviceTraffic>()
        val seenMacs = mutableSetOf<String>()
        
        // Parse Wi-Fi: mac rx tx iface freq
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
        }
        
        // Add remaining from ARP/Leases as LAN (no byte counters available)
        for ((mac, ip) in macToIp) {
            if (!seenMacs.contains(mac) && ip != routerIp && ip != "127.0.0.1") {
                val hostname = macToHostname[mac] ?: "Unknown"
                devices.add(DeviceTraffic(mac, ip, hostname, "LAN", 0L, 0L))
            }
        }
        
        return devices
    }"""

new_parse_devices = """    private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String, routerIp: String): List<DeviceTraffic> {
        val macToHostname = mutableMapOf<String, String>()
        val macToIp = mutableMapOf<String, String>()
        val activeMacs = mutableSetOf<String>()
        val macToInterface = mutableMapOf<String, String>()
        
        // Parse Leases: timestamp mac ip hostname clientid
        for (line in leasesPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 4) {
                val mac = parts[1].lowercase()
                val ip = parts[2]
                val hostname = parts[3].takeIf { it != "*" } ?: "Unknown"
                macToHostname[mac] = hostname
                macToIp[mac] = ip
            }
        }
        
        // Parse ARP: IP HW type Flags HW address Mask Device
        for (line in arpPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 6 && parts[3] != "00:00:00:00:00:00") {
                val flags = parts[2]
                if (flags != "0x0") { // 0x0 is incomplete/offline
                    val ip = parts[0]
                    val mac = parts[3].lowercase()
                    val dev = parts[5]
                    
                    if (dev.startsWith("br-") || dev.startsWith("eth") || dev.startsWith("lan")) {
                        activeMacs.add(mac)
                        macToInterface[mac] = dev
                        if (!macToIp.containsKey(mac)) {
                            macToIp[mac] = ip
                        }
                    }
                }
            }
        }
        
        val devices = mutableListOf<DeviceTraffic>()
        val seenMacs = mutableSetOf<String>()
        
        // Parse Wi-Fi: mac rx tx iface freq
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
        }
        
        // Add remaining active Ethernet (LAN) devices
        for (mac in activeMacs) {
            val ip = macToIp[mac] ?: continue
            if (!seenMacs.contains(mac) && ip != routerIp && ip != "127.0.0.1") {
                val hostname = macToHostname[mac] ?: "Unknown"
                val dev = macToInterface[mac] ?: "LAN"
                val connectionType = if (dev.startsWith("eth") || dev.startsWith("lan")) "Ethernet" else "LAN"
                devices.add(DeviceTraffic(mac, ip, hostname, connectionType, 0L, 0L))
            }
        }
        
        return devices
    }"""

content = content.replace(old_parse_devices, new_parse_devices)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)

