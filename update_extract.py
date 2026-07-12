import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_extract = """            val arpPart = extractSection(stdout, "===ARP===", null)

            val rxTxLines = rxTxPart.lines().map { it.trim() }.filter { it.isNotEmpty() }
            val rx = rxTxLines.getOrNull(0)?.toLongOrNull() ?: 0L
            val tx = rxTxLines.getOrNull(1)?.toLongOrNull() ?: 0L

            val cpuUsage = parseCpuUsage(cpuPart)
            val memoryUsage = parseMemoryUsage(memPart.lines())
            val uptime = parseUptime(uptimePart)

            val devices = parseDevices(wifiPart, leasesPart, arpPart, config.ipAddress)"""

new_extract = """            val arpPart = extractSection(stdout, "===ARP===", "===NLBWMON===")
            val nlbwPart = extractSection(stdout, "===NLBWMON===", null)

            val rxTxLines = rxTxPart.lines().map { it.trim() }.filter { it.isNotEmpty() }
            val rx = rxTxLines.getOrNull(0)?.toLongOrNull() ?: 0L
            val tx = rxTxLines.getOrNull(1)?.toLongOrNull() ?: 0L

            val cpuUsage = parseCpuUsage(cpuPart)
            val memoryUsage = parseMemoryUsage(memPart.lines())
            val uptime = parseUptime(uptimePart)

            val devices = parseDevices(wifiPart, leasesPart, arpPart, nlbwPart, config.ipAddress)"""

if old_extract in content:
    content = content.replace(old_extract, new_extract)
    with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
        f.write(content)
    print("extract updated")
else:
    print("extract not found")
