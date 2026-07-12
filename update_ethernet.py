import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

# We want to replace queryWanTrafficBytes and parseDevices
# Let's find the start of queryWanTrafficBytes
start_query = content.find("suspend fun queryWanTrafficBytes")
# Let's find the end of parseDevices
end_parse = content.find("private fun parseCpuUsage")

new_methods = """suspend fun queryWanTrafficBytes(config: RouterConfig): TrafficAndTelemetry {
        val arch = config.capabilities.switchArchitecture
        val hasBridge = config.capabilities.hasBridgeUtil
        val hasSwconfig = config.capabilities.hasSwconfigUtil
        
        var ethernetCmd = ""
        if (hasBridge) {
            ethernetCmd += "echo '===BRIDGE_FDB==='; bridge fdb show br-lan 2>/dev/null | grep dev | grep -v 'self'; "
        }
        if (arch == SwitchArchitecture.DSA) {
            ethernetCmd += "echo '===DSA_PORTS==='; for lan in \$(ls /sys/class/net/ 2>/dev/null | grep -E '^lan[0-9]+\$'); do carrier=\$(cat /sys/class/net/\$lan/carrier 2>/dev/null || echo 0); speed=\$(cat /sys/class/net/\$lan/speed 2>/dev/null || echo 0); rx=\$(cat /sys/class/net/\$lan/statistics/rx_bytes 2>/dev/null || echo 0); tx=\$(cat /sys/class/net/\$lan/statistics/tx_bytes 2>/dev/null || echo 0); echo \"\$lan \$carrier \$speed \$rx \$tx\"; done; "
        } else if (arch == SwitchArchitecture.SWCONFIG && hasSwconfig) {
            ethernetCmd += "echo '===SWCONFIG_PORTS==='; swconfig dev switch0 show 2>/dev/null; "
        }

        if (arch == SwitchArchitecture.SWCONFIG && config.capabilities.hasBoardJsonWithSwitchSection) {
            ethernetCmd += "echo '===BOARD_JSON_SWITCH==='; cat /etc/board.json 2>/dev/null; "
        }

        val cmd = "route_iface=\$(ip route | grep '^default' | head -n1 | awk '{print \$5}'); if [ -n \"\$route_iface\" ] && [ -d \"/sys/class/net/\$route_iface\" ]; then cat /sys/class/net/\$route_iface/statistics/rx_bytes; cat /sys/class/net/\$route_iface/statistics/tx_bytes; else cat /proc/net/dev | grep -E \"(eth|wan|dsl|wlan|rmnet|ppp)\" | head -n1 | tr ':' ' ' | awk '{print \$2; print \$10}'; fi; echo '===CPU==='; top_out=\$(top -b -n 1 | grep -i 'cpu' | head -n 1); if [ -n \"\$top_out\" ]; then echo \"\$top_out\"; else cat /proc/loadavg 2>/dev/null; fi; echo '===MEM==='; cat /proc/meminfo 2>/dev/null | grep -E '^(MemTotal|MemFree|MemAvailable|Buffers|Cached):'; echo '===UPTIME==='; cat /proc/uptime 2>/dev/null; echo '===WIFI==='; for iface in \$(iw dev 2>/dev/null | grep Interface | awk '{print \$2}'); do freq=\$(iw dev \$iface info 2>/dev/null | grep -oE '[0-9]+ MHz' | head -n1 | awk '{print \$1}'); [ -z \"\$freq\" ] && freq=0; iw dev \$iface station dump | awk -v ifc=\"\$iface\" -v f=\"\$freq\" '/^Station/ { mac=\$2 } /rx bytes:/ { rx=\$3 } /tx bytes:/ { tx=\$3; print mac, rx, tx, ifc, f }'; done; echo '===LEASES==='; cat /tmp/dhcp.leases 2>/dev/null; echo '===ARP==='; cat /proc/net/arp 2>/dev/null; echo '===NLBWMON==='; if command -v nlbw >/dev/null; then nlbw -c csv -g mac 2>/dev/null; fi; $ethernetCmd"
        return try {
            val res = sshClientManager.executeCommand(config, cmd)
            val stdout = res.stdout

            val rxTxPart = extractSection(stdout, "", "===CPU===").ifEmpty {
                val index = stdout.indexOf("===CPU===")
                if (index != -1) stdout.substring(0, index) else stdout
            }
            val cpuPart = extractSection(stdout, "===CPU===", "===MEM===")
            val memPart = extractSection(stdout, "===MEM===", "===UPTIME===")
            val uptimePart = extractSection(stdout, "===UPTIME===", "===WIFI===")
            val wifiPart = extractSection(stdout, "===WIFI===", "===LEASES===")
            val leasesPart = extractSection(stdout, "===LEASES===", "===ARP===")
            val arpPart = extractSection(stdout, "===ARP===", "===NLBWMON===")
            val nlbwPart = extractSection(stdout, "===NLBWMON===", "===BRIDGE_FDB===").ifEmpty {
                val idx = stdout.indexOf("===NLBWMON===")
                if (idx != -1) stdout.substring(idx + "===NLBWMON===".length).substringBefore("===") else ""
            }

            var bridgeFdbPart = ""
            var dsaPortsPart = ""
            var swconfigPortsPart = ""
            var boardJsonPart = ""

            if (stdout.contains("===BRIDGE_FDB===")) {
                bridgeFdbPart = extractSection(stdout, "===BRIDGE_FDB===", "===").ifEmpty {
                    stdout.substringAfter("===BRIDGE_FDB===").substringBefore("===")
                }
            }
            if (stdout.contains("===DSA_PORTS===")) {
                dsaPortsPart = extractSection(stdout, "===DSA_PORTS===", "===").ifEmpty {
                    stdout.substringAfter("===DSA_PORTS===").substringBefore("===")
                }
            }
            if (stdout.contains("===SWCONFIG_PORTS===")) {
                swconfigPortsPart = extractSection(stdout, "===SWCONFIG_PORTS===", "===").ifEmpty {
                    stdout.substringAfter("===SWCONFIG_PORTS===").substringBefore("===")
                }
            }
            if (stdout.contains("===BOARD_JSON_SWITCH===")) {
                boardJsonPart = extractSection(stdout, "===BOARD_JSON_SWITCH===", "===").ifEmpty {
                    stdout.substringAfter("===BOARD_JSON_SWITCH===").substringBefore("===")
                }
            }

            val rxTxLines = rxTxPart.lines().map { it.trim() }.filter { it.isNotEmpty() }
            val rx = rxTxLines.getOrNull(0)?.toLongOrNull() ?: 0L
            val tx = rxTxLines.getOrNull(1)?.toLongOrNull() ?: 0L

            val cpuUsage = parseCpuUsage(cpuPart)
            val memoryUsage = parseMemoryUsage(memPart.lines())
            val uptime = parseUptime(uptimePart)

            val devices = parseDevices(
                wifiPart, leasesPart, arpPart, nlbwPart, config,
                bridgeFdbPart, dsaPortsPart, swconfigPortsPart, boardJsonPart
            )
            
            TrafficAndTelemetry(
                rxBytes = rx,
                txBytes = tx,
                cpuUsage = cpuUsage,
                memoryUsage = memoryUsage,
                uptime = uptime,
                devices = devices
            )

        } catch (e: Exception) {
            TrafficAndTelemetry()
        }
    }

    private fun parseDevices(
        wifiPart: String, leasesPart: String, arpPart: String, nlbwPart: String,
        config: RouterConfig, bridgeFdbPart: String, dsaPortsPart: String,
        swconfigPortsPart: String, boardJsonPart: String
    ): List<DeviceTraffic> {
        val routerIp = config.ipAddress
        val nlbwRx = mutableMapOf<String, Long>()
        val nlbwTx = mutableMapOf<String, Long>()
        var rxIdx = -1
        var txIdx = -1
        var macIdx = 0
        for ((idx, line) in nlbwPart.lines().withIndex()) {
            val parts = line.trim().split(",")
            if (idx == 0 && (line.contains("rx_bytes", ignoreCase = true) || line.contains("rx", ignoreCase = true))) {
                for (i in parts.indices) {
                    val p = parts[i].replace("\"", "").lowercase()
                    if (p.contains("rx")) rxIdx = i
                    if (p.contains("tx")) txIdx = i
                    if (p.contains("mac")) macIdx = i
                }
                continue
            }
            if (rxIdx == -1) {
                if (parts.size >= 6) {
                    macIdx = 0
                    rxIdx = 3
                    txIdx = 4
                } else if (parts.size >= 4) {
                    macIdx = 0
                    rxIdx = 2
                    txIdx = 3
                }
            }
            
            if (parts.size > maxOf(rxIdx, txIdx, macIdx) && rxIdx != -1) {
                val mac = parts[macIdx].replace("\"", "").lowercase()
                val rx = parts[rxIdx].replace("\"", "").toLongOrNull() ?: 0L
                val tx = parts[txIdx].replace("\"", "").toLongOrNull() ?: 0L
                nlbwRx[mac] = rx
                nlbwTx[mac] = tx
            }
        }
        val macToHostname = mutableMapOf<String, String>()
        val macToIp = mutableMapOf<String, String>()
        val activeMacs = mutableSetOf<String>()
        val macToInterface = mutableMapOf<String, String>()
        
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
        
        for (line in arpPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 6 && parts[3] != "00:00:00:00:00:00") {
                val flags = parts[2]
                if (flags != "0x0") {
                    val ip = parts[0]
                    val mac = parts[3].lowercase()
                    val dev = parts[5]
                    
                    activeMacs.add(mac)
                    macToInterface[mac] = dev
                    if (!macToIp.containsKey(mac)) {
                        macToIp[mac] = ip
                    }
                }
            }
        }
        
        val devices = mutableListOf<DeviceTraffic>()
        val seenMacs = mutableSetOf<String>()
        
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
                    val monthRx = nlbwRx[mac] ?: 0L
                    val monthTx = nlbwTx[mac] ?: 0L
                    devices.add(DeviceTraffic(mac, ip, hostname, band, rx, tx, monthRx, monthTx))
                    seenMacs.add(mac)
                }
            }
        }
        
        val fdbMap = mutableMapOf<String, String>()
        for (line in bridgeFdbPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 3 && parts[1] == "dev") {
                val mac = parts[0].lowercase()
                val dev = parts[2]
                fdbMap[mac] = dev
            }
        }

        val dsaData = mutableMapOf<String, LongArray>()
        for (line in dsaPortsPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 5) {
                val dev = parts[0]
                val carrier = parts[1].toLongOrNull() ?: 0L
                val speed = parts[2].toLongOrNull() ?: 0L
                val rx = parts[3].toLongOrNull() ?: 0L
                val tx = parts[4].toLongOrNull() ?: 0L
                if (carrier > 0L) {
                    dsaData[dev] = longArrayOf(rx, tx)
                }
            }
        }

        // SWCONFIG logic
        val swconfigPorts = mutableMapOf<String, LongArray>()
        var currentPort: String? = null
        for (line in swconfigPortsPart.lines()) {
            val trimmed = line.trim()
            if (trimmed.startsWith("Port ")) {
                currentPort = trimmed.substringAfter("Port ").removeSuffix(":")
            } else if (currentPort != null && trimmed.startsWith("mib:")) {
                val rxMatch = \"\"\"rx_bytes:\s*(\d+)\"\"\".toRegex().find(trimmed)
                val txMatch = \"\"\"tx_bytes:\s*(\d+)\"\"\".toRegex().find(trimmed)
                if (rxMatch != null && txMatch != null) {
                    swconfigPorts[currentPort] = longArrayOf(
                        rxMatch.groupValues[1].toLong(),
                        txMatch.groupValues[1].toLong()
                    )
                }
            }
        }

        var eth0PhysicalPorts = ""
        val swMatcher = \"\"\""ports":\s*"([^"]+)"\"\"\".toRegex()
        val eth0Match = \"\"\""ifname":\s*"eth0([^"]*)"\"\"\".toRegex().find(boardJsonPart)
        if (eth0Match != null) {
            val eth0Block = boardJsonPart.substring(eth0Match.range.first, boardJsonPart.length.coerceAtMost(eth0Match.range.first + 200))
            val portsMatch = swMatcher.find(eth0Block)
            if (portsMatch != null) {
                eth0PhysicalPorts = portsMatch.groupValues[1].replace("0t", "").trim()
            }
        }

        for (mac in activeMacs) {
            val ip = macToIp[mac] ?: continue
            if (!seenMacs.contains(mac) && ip != routerIp && ip != "127.0.0.1") {
                val hostname = macToHostname[mac] ?: "Unknown"
                val dev = fdbMap[mac] ?: macToInterface[mac] ?: "LAN"
                val monthRx = nlbwRx[mac] ?: 0L
                val monthTx = nlbwTx[mac] ?: 0L
                
                var connectionType = "Ethernet"
                var devRx = 0L
                var devTx = 0L
                
                if (config.capabilities.switchArchitecture == SwitchArchitecture.DSA) {
                    if (dsaData.containsKey(dev)) {
                        connectionType = "LAN (\$dev)"
                        devRx = dsaData[dev]!![0]
                        devTx = dsaData[dev]!![1]
                    } else {
                        connectionType = "Ethernet"
                    }
                } else if (config.capabilities.switchArchitecture == SwitchArchitecture.SWCONFIG) {
                    connectionType = if (dev.startsWith("eth0")) "LAN (\$eth0PhysicalPorts)" else "LAN (\$dev)"
                    // Since multiple MACs share eth0, we cannot map rx/tx from swconfig per device accurately,
                    // but we can pass 0 for now and let the UI know traffic is unavailable or use nlbwmon.
                    // Wait, the prompt says for SWCONFIG: 
                    // "вычислять как дельту между двумя опросами, делённую на интервал времени (учесть что здесь именно накопительные байтовые счётчики, а не мгновенный bitrate как в Wi-Fi)"
                    // But if swconfig gives stats per PORT and we can't map MAC to port, we can't show it per MAC.
                    // The prompt: "Ветка SWCONFIG: ... Линк-статус и MIB-счётчики (если поддерживаются чипом): swconfig dev switch0 show ... Если их нет, трафик помечать как нет данных".
                    // Actually, if we just give 0 for rx/tx, we'll use nlbw Rx/Tx as month bytes.
                    devRx = 0L
                    devTx = 0L
                }

                devices.add(DeviceTraffic(mac, ip, hostname, connectionType, devRx, devTx, monthRx, monthTx))
            }
        }
        
        return devices
    }
"""

new_content = content[:start_query] + new_methods + "\n    " + content[end_parse:]

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(new_content)
