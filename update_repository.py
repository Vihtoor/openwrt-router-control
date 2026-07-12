import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

# Add DeviceTraffic data class
dt_class = """
data class DeviceTraffic(
    val mac: String,
    val ip: String,
    val hostname: String,
    val connectionType: String,
    val rxBytes: Long,
    val txBytes: Long
)
"""

if "data class DeviceTraffic" not in content:
    content = content.replace("data class TrafficAndTelemetry(", dt_class + "\ndata class TrafficAndTelemetry(")

# Add devices to TrafficAndTelemetry
if "val devices: List<DeviceTraffic>" not in content:
    content = content.replace("val uptime: String = \"—\"", "val uptime: String = \"—\",\n    val devices: List<DeviceTraffic> = emptyList()")

# Update queryWanTrafficBytes cmd
old_cmd = """val cmd = "route_iface=\\$(ip route | grep '^default' | head -n1 | awk '{print \\$5}'); if [ -n \\"\\$route_iface\\" ] && [ -d \\"/sys/class/net/\\$route_iface\\" ]; then cat /sys/class/net/\\$route_iface/statistics/rx_bytes; cat /sys/class/net/\\$route_iface/statistics/tx_bytes; else cat /proc/net/dev | grep -E \\"(eth|wan|dsl|wlan|rmnet|ppp)\\" | head -n1 | tr ':' ' ' | awk '{print \\$2; print \\$10}'; fi; echo '===CPU==='; top_out=\\$(top -b -n 1 | grep -i 'cpu' | head -n 1); if [ -n \\"\\$top_out\\" ]; then echo \\"\\$top_out\\"; else cat /proc/loadavg 2>/dev/null; fi; echo '===MEM==='; cat /proc/meminfo 2>/dev/null | grep -E '^(MemTotal|MemFree|MemAvailable|Buffers|Cached):'; echo '===UPTIME==='; cat /proc/uptime 2>/dev/null\""""

new_cmd = """val cmd = "route_iface=\\$(ip route | grep '^default' | head -n1 | awk '{print \\$5}'); if [ -n \\"\\$route_iface\\" ] && [ -d \\"/sys/class/net/\\$route_iface\\" ]; then cat /sys/class/net/\\$route_iface/statistics/rx_bytes; cat /sys/class/net/\\$route_iface/statistics/tx_bytes; else cat /proc/net/dev | grep -E \\"(eth|wan|dsl|wlan|rmnet|ppp)\\" | head -n1 | tr ':' ' ' | awk '{print \\$2; print \\$10}'; fi; echo '===CPU==='; top_out=\\$(top -b -n 1 | grep -i 'cpu' | head -n 1); if [ -n \\"\\$top_out\\" ]; then echo \\"\\$top_out\\"; else cat /proc/loadavg 2>/dev/null; fi; echo '===MEM==='; cat /proc/meminfo 2>/dev/null | grep -E '^(MemTotal|MemFree|MemAvailable|Buffers|Cached):'; echo '===UPTIME==='; cat /proc/uptime 2>/dev/null; echo '===WIFI==='; for iface in \\$(iw dev 2>/dev/null | grep Interface | awk '{print \\$2}'); do iw dev \\$iface station dump | awk -v ifc=\\"\\$iface\\" '/^Station/ { mac=\\$2 } /rx bytes:/ { rx=\\$3 } /tx bytes:/ { tx=\\$3; print mac, rx, tx, ifc }'; done; echo '===LEASES==='; cat /tmp/dhcp.leases 2>/dev/null; echo '===ARP==='; cat /proc/net/arp 2>/dev/null\""""

content = content.replace(old_cmd, new_cmd)

# Parse output
old_parse = """            val uptimePart = extractSection(stdout, "===UPTIME===", null)
            val rxTxLines = rxTxPart.lines().map { it.trim() }.filter { it.isNotEmpty() }"""

new_parse = """            val uptimePart = extractSection(stdout, "===UPTIME===", "===WIFI===")
            val wifiPart = extractSection(stdout, "===WIFI===", "===LEASES===")
            val leasesPart = extractSection(stdout, "===LEASES===", "===ARP===")
            val arpPart = extractSection(stdout, "===ARP===", null)

            val rxTxLines = rxTxPart.lines().map { it.trim() }.filter { it.isNotEmpty() }
            val rx = rxTxLines.getOrNull(0)?.toLongOrNull() ?: 0L
            val tx = rxTxLines.getOrNull(1)?.toLongOrNull() ?: 0L

            val cpuUsage = parseCpuUsage(cpuPart)
            val memoryUsage = parseMemoryUsage(memPart.lines())
            val uptime = parseUptime(uptimePart)

            val devices = parseDevices(wifiPart, leasesPart, arpPart)
            
            TrafficAndTelemetry(
                rxBytes = rx,
                txBytes = tx,
                cpuUsage = cpuUsage,
                memoryUsage = memoryUsage,
                uptime = uptime,
                devices = devices
            )
"""

if "parseDevices" not in content:
    # replace the return block completely
    import re
    content = re.sub(r'val uptimePart = extractSection\(stdout, "===UPTIME===", null\).*?TrafficAndTelemetry\([^)]*\)', new_parse, content, flags=re.DOTALL)


# Add parseDevices function
parse_devices_func = """
    private fun parseDevices(wifiPart: String, leasesPart: String, arpPart: String): List<DeviceTraffic> {
        val macToHostname = mutableMapOf<String, String>()
        val macToIp = mutableMapOf<String, String>()
        
        // Parse Leases: timestamp mac ip hostname clientid
        for (line in leasesPart.lines()) {
            val parts = line.trim().split("\\s+".toRegex())
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
            val parts = line.trim().split("\\s+".toRegex())
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
        
        // Parse Wi-Fi: mac rx tx iface
        for (line in wifiPart.lines()) {
            val parts = line.trim().split("\\s+".toRegex())
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
        }
        
        // Add remaining from ARP/Leases as LAN (no byte counters available)
        for ((mac, ip) in macToIp) {
            if (!seenMacs.contains(mac)) {
                val hostname = macToHostname[mac] ?: "Unknown"
                devices.add(DeviceTraffic(mac, ip, hostname, "LAN", 0L, 0L))
            }
        }
        
        return devices
    }
"""

if "fun parseDevices" not in content:
    content = content.replace("private fun parseCpuUsage", parse_devices_func + "\n    private fun parseCpuUsage")

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)
