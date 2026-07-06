package com.example.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

data class RouterStatus(
    val isOpenVpnActive: Boolean = false,
    val isWireGuardActive: Boolean = false,
    val isLedActive: Boolean = true,
    val isLedFileValid: Boolean = false,
    val publicIp: String = "Connecting...",
    val location: String = "Detecting...",
    val provider: String = "Detecting...",
    val openVpnInstanceName: String? = null,
    val activeWgInterface: String? = null,
    val activeOpenVpnInstances: String? = null,
    val model: String = "Detecting...",
    val version: String = "Detecting...",
    val dnsServers: String = "",
    val isCustomDns: Boolean = false
)

data class TrafficAndTelemetry(
    val rxBytes: Long = 0L,
    val txBytes: Long = 0L,
    val cpuUsage: String = "—",
    val memoryUsage: String = "—",
    val uptime: String = "—"
)

class RouterRepository(
    private val dao: RouterDao,
    private val sshClientManager: SshClientManager
) {
    companion object {
        @Volatile var lastTriggerName: String? = null
        @Volatile var hasUciTriggerError: Boolean = false
    }

    val routerConfigFlow: Flow<RouterConfig?> = dao.getActiveRouterConfigFlow()
    val allRouterConfigsFlow: Flow<List<RouterConfig>> = dao.getAllRouterConfigsFlow()
    val consoleLogsFlow: Flow<List<ConsoleLog>> = dao.getConsoleLogsFlow()

    suspend fun saveRouterConfig(config: RouterConfig): Long {
        if (config.isActive) {
            dao.deactivateAll()
        }
        return dao.saveRouterConfig(config)
    }

    suspend fun deleteRouterConfig(id: Int) {
        dao.deleteRouterConfig(id)
        sshClientManager.disconnect()
    }

    suspend fun insertConsoleLog(command: String, output: String): Long {
        return dao.insertConsoleLog(ConsoleLog(command = command, output = output))
    }

    suspend fun updateConsoleLog(id: Long, command: String, output: String) {
        dao.insertConsoleLog(ConsoleLog(id = id, command = command, output = output))
    }

    suspend fun clearConsoleLogs() {
        dao.clearConsoleLogs()
    }

    suspend fun deleteConsoleLogsByCommand(command: String) {
        dao.deleteConsoleLogsByCommand(command)
    }

    suspend fun testConnection(config: RouterConfig): Boolean {
        return sshClientManager.testConnection(config)
    }

    suspend fun startInteractiveShell(config: RouterConfig, onOutput: (String) -> Unit) {
        sshClientManager.startInteractiveShell(config, onOutput)
    }

    suspend fun stopInteractiveShell() {
        sshClientManager.stopInteractiveShell()
    }

    fun writeToConsoleStdin(text: String): Boolean {
        return sshClientManager.writeToStdin(text)
    }

    fun writeRawToConsoleStdin(text: String): Boolean {
        return sshClientManager.writeRawToStdin(text)
    }

    suspend fun executeConsoleCommand(config: RouterConfig, command: String, saveToLog: Boolean = true): String {
        if (!saveToLog) {
            return try {
                val timeout = if (command.contains("iperf3")) 45000L else 10000L
                val result = sshClientManager.executeCommand(config, command, timeout)
                result.fullOutput()
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
        val logId = insertConsoleLog(command, "...")
        return try {
            val timeout = if (command.contains("iperf3")) 45000L else 10000L
            val result = sshClientManager.executeCommand(config, command, timeout) { partialOutput ->
                updateConsoleLog(logId, command, partialOutput)
            }
            val fullOut = result.fullOutput()
            updateConsoleLog(logId, command, fullOut)
            fullOut
        } catch (e: Exception) {
            val errorOut = "Error: ${e.message}"
            updateConsoleLog(logId, command, errorOut)
            errorOut
        }
    }

private fun isValidOpenVpnProcessLine(line: String): Boolean {
        val lower = line.lowercase()
        return lower.contains("openvpn") && 
            !lower.contains("grep") && 
            !lower.contains("echo") && 
            !lower.contains("ps |") &&
            !lower.contains("sh -c") &&
            !lower.contains("bin/sh") &&
            !lower.contains("ps ") &&
            !lower.contains("uci ") &&
            !lower.contains("export path=") &&
            !lower.contains("/etc/init.d/openvpn") &&
            !lower.contains("logread") &&
            !lower.contains("tail ") &&
            !lower.contains("cat ") &&
            !lower.contains("vi ") &&
            !lower.contains("nano ") &&
            !lower.contains("awk ") &&
            !lower.contains("sed ")
    }

    private fun extractSection(input: String, startTag: String, endTag: String?): String {
        val startIndex = input.indexOf(startTag)
        if (startIndex == -1) return ""
        val actualStart = startIndex + startTag.length
        val endIndex = if (endTag != null) input.indexOf(endTag, actualStart) else -1
        return if (endIndex != -1) {
            input.substring(actualStart, endIndex).trim()
        } else {
            input.substring(actualStart).trim()
        }
    }

    suspend fun queryRouterStatus(config: RouterConfig): RouterStatus {
        val wgIface = config.wgInterface
        val combinedCmd = "echo \'===OPENVPN===\'; ps -w | grep \'[o]penvpn\'; echo \'===WG===\'; ifstatus $wgIface 2>/dev/null || ifconfig $wgIface 2>/dev/null; echo \'===ACTIVE_WGS===\'; for dev in \$(uci show network 2>/dev/null | grep =interface | cut -d. -f2 | cut -d= -f1); do if [ -n \"\$dev\" ] && [ \"\$dev\" != \"lan\" ] && [ \"\$dev\" != \"wan\" ] && [ \"\$dev\" != \"wan6\" ] && [ \"\$dev\" != \"loopback\" ]; then proto=\$(uci -q get network.\$dev.proto); if [ \"\$proto\" = \"wireguard\" ] || [ \"\$proto\" = \"amneziawg\" ]; then if ifstatus \$dev 2>/dev/null | grep -q '\"up\": true' || ifconfig \$dev 2>/dev/null | grep -q 'UP'; then echo \$dev; fi; fi; fi; done; echo \'===LED===\'; for i in 1 2 3 4; do b=\$(cat /sys/class/leds/blue:status/brightness 2>/dev/null || echo 0); t=\$(cat /sys/class/leds/blue:status/trigger 2>/dev/null || echo \"[none]\"); if [ \"\$b\" -gt 0 ] || ! echo \"\$t\" | grep -F -q '[none]'; then echo 1; else echo 0; fi; usleep 200000 2>/dev/null || sleep 1 2>/dev/null || true; done; echo \'===LED_FILE===\'; if [ -f /sys/class/leds/blue:status/brightness ]; then val=\$(cat /sys/class/leds/blue:status/brightness 2>/dev/null); if [ \"\$val\" = \"0\" ] || [ \"\$val\" = \"1\" ]; then echo \"1\"; else echo \"0\"; fi; else echo \"0\"; fi; echo \'===IP===\'; (curl -s --connect-timeout 2 --max-time 3 https://ipinfo.io/json || wget -T 2 -t 1 -qO- https://ipinfo.io/json || wget -T 2 -t 1 -qO- http://ip-api.com/json) 2>/dev/null; echo \'===MODEL===\'; cat /tmp/sysinfo/model 2>/dev/null || cat /proc/cpuinfo | grep -E -i \'machine|hardware\' | cut -d: -f2 | head -n1 || echo \'OpenWrt\'; echo \'===VERSION===\'; (grep DISTRIB_RELEASE /etc/openwrt_release 2>/dev/null || cat /etc/openwrt_version 2>/dev/null || echo \'OpenWrt\'); echo \'===LED_TRIGGER===\'; uci get system.@led[0].trigger 2>&1; echo \'===IS_CUSTOM_DNS===\'; peerdns=\$(uci -q get network.wan.peerdns); if [ \"\$peerdns\" = \"0\" ]; then echo 1; else echo 0; fi; echo \'===DNS===\'; if [ \"\$peerdns\" = \"1\" ] || [ -z \"\$peerdns\" ]; then cat /tmp/resolv.conf.auto 2>/dev/null | grep nameserver | awk '{print \$2}' | tr '\n' ',' | sed 's/,\$//'; else uci -q get network.wan.dns | tr ' ' ','; fi"
        
        return try {
            val res = sshClientManager.executeCommand(config, combinedCmd)
            val stdout = res.stdout
            
            val openVpnPart = extractSection(stdout, "===OPENVPN===", "===WG===")
            val wgPart = extractSection(stdout, "===WG===", "===ACTIVE_WGS===")
            val activeWgsPart = extractSection(stdout, "===ACTIVE_WGS===", "===LED===")
            val ledPart = extractSection(stdout, "===LED===", "===LED_FILE===")
            val ledFilePart = extractSection(stdout, "===LED_FILE===", "===IP===")
            val ipPart = extractSection(stdout, "===IP===", "===MODEL===")
            val modelPart = extractSection(stdout, "===MODEL===", "===VERSION===").trim().ifEmpty { "OpenWrt" }
            val versionSection = extractSection(stdout, "===VERSION===", "===LED_TRIGGER===").trim()
            val ledTriggerPart = extractSection(stdout, "===LED_TRIGGER===", "===IS_CUSTOM_DNS===").trim()
            val isCustomDnsPart = extractSection(stdout, "===IS_CUSTOM_DNS===", "===DNS===").trim()
            val dnsPart = extractSection(stdout, "===DNS===", null).trim()

            if (ledTriggerPart.contains("Entry not found", ignoreCase = true)) {
                hasUciTriggerError = true
            } else if (ledTriggerPart.isNotEmpty()) {
                hasUciTriggerError = false
                if (ledTriggerPart != "none" && !ledTriggerPart.contains("error", ignoreCase = true)) {
                    lastTriggerName = ledTriggerPart
                }
            }

            val parsedVersion = if (versionSection.contains("DISTRIB_RELEASE=")) {
                versionSection.substringAfter("DISTRIB_RELEASE=")
                    .replace("\"", "")
                    .replace("'", "")
                    .trim()
            } else {
                versionSection
            }.ifEmpty { "OpenWrt" }
            
            // Parse OpenVPN
            val isOpenVpnActive = openVpnPart.lines().any { line ->
                isValidOpenVpnProcessLine(line)
            }
            val vpnInstanceName = if (isOpenVpnActive) extractOpenVpnInstanceName(openVpnPart) else null
            val activeOpenVpnNamesSet = if (isOpenVpnActive) extractAllOpenVpnInstanceNames(openVpnPart) else emptySet()
            val activeOpenVpnNamesStr = activeOpenVpnNamesSet.joinToString(",")
            
            // Parse WG
            val isWireGuardActive = if (wgPart.contains("\"up\":")) {
                wgPart.contains("\"up\": true")
            } else {
                wgPart.isNotEmpty() && 
                wgPart.contains("UP") &&
                !wgPart.contains("Interface $wgIface not found") && 
                !wgPart.contains("Device not found") &&
                !wgPart.contains("error")
            }
            
            // Parse active WG interfaces
            val activeWgsList = activeWgsPart.lines().map { it.trim() }.filter { it.isNotEmpty() }
            val activeWgsStr = activeWgsList.joinToString(",")
            
            // Parse LED
            val isLedFileValid = ledFilePart.trim() == "1"
            val ledValues = ledPart.split("\\s+".toRegex()).mapNotNull { it.trim().toIntOrNull() }
            val isLedActive = ledValues.any { it > 0 }
            
            // Parse IP
            val parsedIpInfo = parseIpInfo(ipPart)
            var ip = parsedIpInfo.first
            var location = parsedIpInfo.second
            var provider = parsedIpInfo.third

            if (ip == "Unknown" || location == "Unknown" || provider == "Unknown") {
                val ipCmd = "(curl -s --connect-timeout 2 --max-time 3 https://ipinfo.io/json || wget -T 2 -t 1 -qO- https://ipinfo.io/json || wget -T 2 -t 1 -qO- http://ip-api.com/json) 2>/dev/null"
                for (attempt in 1..3) {
                    kotlinx.coroutines.delay(1200)
                    try {
                        val retryRes = sshClientManager.executeCommand(config, ipCmd)
                        val retryIpPart = retryRes.stdout.trim()
                        val (rIp, rLoc, rProv) = parseIpInfo(retryIpPart)
                        if (rIp != "Unknown" && rLoc != "Unknown" && rProv != "Unknown") {
                            ip = rIp
                            location = rLoc
                            provider = rProv
                            break
                        }
                    } catch (retryEx: Exception) {
                        Log.e("RouterRepository", "Retry $attempt to fetch IP info failed: ${retryEx.message}")
                    }
                }
            }
            
            val dnsServersStr = dnsPart.split(",").map { it.trim() }.filter { it.isNotEmpty() }.joinToString(", ")
            val isCustomDns = isCustomDnsPart == "1"
            
            RouterStatus(
                isOpenVpnActive = isOpenVpnActive,
                isWireGuardActive = isWireGuardActive,
                isLedActive = isLedActive,
                isLedFileValid = isLedFileValid,
                publicIp = ip,
                location = location,
                provider = provider,
                openVpnInstanceName = vpnInstanceName,
                activeWgInterface = activeWgsStr,
                activeOpenVpnInstances = activeOpenVpnNamesStr,
                model = modelPart,
                version = parsedVersion,
                dnsServers = dnsServersStr,
                isCustomDns = isCustomDns
            )
        } catch (e: Exception) {
            Log.e("RouterRepository", "Failed to query status in combined mode: ${e.message}", e)
            try {
                // Minimal quick fallback if combined query throws exception
                val openVpnCheck = try {
                    val res = sshClientManager.executeCommand(config, "ps -w | grep '[o]penvpn'")
                    res.stdout.lines().any { line ->
                        isValidOpenVpnProcessLine(line)
                    }
                } catch (ex: Exception) { false }
                
                val wireguardCheck = try {
                    val res = sshClientManager.executeCommand(config, "ifstatus $wgIface 2>/dev/null || ifconfig $wgIface 2>/dev/null")
                    val out = res.stdout
                    if (out.contains("\"up\":")) {
                        out.contains("\"up\": true")
                    } else {
                        out.isNotEmpty() && out.contains("UP") && !out.contains("Interface $wgIface not found") && !out.contains("Device not found") && !out.contains("error")
                    }
                } catch (ex: Exception) { false }
                
                RouterStatus(
                    isOpenVpnActive = openVpnCheck,
                    isWireGuardActive = wireguardCheck,
                    isLedActive = false,
                    publicIp = "Offline / Error",
                    location = "Unavailable",
                    provider = "Unavailable",
                    model = "Offline / Error",
                    version = "Offline / Error"
                )
            } catch (ex: Exception) {
                RouterStatus(
                    isOpenVpnActive = false,
                    isWireGuardActive = false,
                    isLedActive = false,
                    publicIp = "Offline / Error",
                    location = "Unavailable",
                    provider = "Unavailable",
                    model = "Offline / Error",
                    version = "Offline / Error"
                )
            }
        }
    }

    suspend fun queryPublicIpOnly(config: RouterConfig): String {
        return try {
            val res = sshClientManager.executeCommand(
                config,
                "curl -s --connect-timeout 2 --max-time 3 https://ipinfo.io/json || wget -T 2 -t 1 -qO- https://ipinfo.io/json || wget -T 2 -t 1 -qO- http://ip-api.com/json"
            )
            val info = parseIpInfo(res.stdout)
            info.first
        } catch (e: Exception) {
            "Offline / Error"
        }
    }

    private fun extractOpenVpnInstanceName(psOutput: String): String? {
        val syslogRegex = """openvpn\(([^)]+)\)""".toRegex()
        val syslogMatch = syslogRegex.find(psOutput)
        if (syslogMatch != null) {
            return syslogMatch.groupValues[1]
        }

        val confRegex = """openvpn-([^.\s/]+)\.conf""".toRegex()
        val confMatch = confRegex.find(psOutput)
        if (confMatch != null) {
            return confMatch.groupValues[1]
        }

        val configParamRegex = """--config\s+(\S+/)?([^/\s.]+)\.conf""".toRegex()
        val configParamMatch = configParamRegex.find(psOutput)
        if (configParamMatch != null) {
            return configParamMatch.groupValues[2]
        }

        val generalConfRegex = """/([^/\s]+)\.conf""".toRegex()
        val generalConfMatch = generalConfRegex.find(psOutput)
        if (generalConfMatch != null) {
            return generalConfMatch.groupValues[1]
        }

        return null
    }

    private fun extractAllOpenVpnInstanceNames(psOutput: String): Set<String> {
        val names = mutableSetOf<String>()
        val syslogRegex = """openvpn\(([^)]+)\)""".toRegex()
        val confRegex = """openvpn-([^.\s/]+)\.conf""".toRegex()
        val configParamRegex = """--config\s+(\S+/)?([^/\s.]+)\.conf""".toRegex()
        val generalConfRegex = """/([^/\s]+)\.conf""".toRegex()

        for (line in psOutput.lines()) {
            if (isValidOpenVpnProcessLine(line)) {
                var foundInLine = false
                
                syslogRegex.find(line)?.let {
                    names.add(it.groupValues[1].trim())
                    foundInLine = true
                }
                if (foundInLine) continue

                confRegex.find(line)?.let {
                    names.add(it.groupValues[1].trim())
                    foundInLine = true
                }
                if (foundInLine) continue

                configParamRegex.find(line)?.let {
                    names.add(it.groupValues[2].trim())
                    foundInLine = true
                }
                if (foundInLine) continue

                generalConfRegex.find(line)?.let {
                    names.add(it.groupValues[1].trim())
                    foundInLine = true
                }
            }
        }
        return names
    }

    suspend fun setOpenVpnStatus(config: RouterConfig, start: Boolean, serviceName: String? = null) {
        val sName = serviceName ?: config.openVpnService
        val command = if (start) {
            "/etc/init.d/openvpn stop; /etc/init.d/openvpn start $sName"
        } else {
            "/etc/init.d/openvpn stop $sName || /etc/init.d/openvpn stop"
        }
        sshClientManager.executeCommand(config, command)
    }

    suspend fun fetchEnabledOpenVpnServices(config: RouterConfig): List<String> {
        val command = "uci show openvpn 2>/dev/null"
        return try {
            val res = sshClientManager.executeCommand(config, command)
            val stdout = res.stdout
            val enabledServices = mutableListOf<String>()
            val regex = """openvpn\.([^.\s]+)\.enabled=['"]?(\w+)['"]?""".toRegex(RegexOption.IGNORE_CASE)
            stdout.lines().forEach { line ->
                val trimmed = line.trim()
                val match = regex.find(trimmed)
                if (match != null) {
                    val sectionName = match.groupValues[1]
                    val enabledVal = match.groupValues[2].lowercase()
                    if (enabledVal == "1" || enabledVal == "yes" || enabledVal == "true" || enabledVal == "on") {
                        enabledServices.add(sectionName)
                    }
                }
            }
            enabledServices.distinct().sorted()
        } catch (e: Exception) {
            Log.e("RouterRepository", "Failed to fetch enabled OpenVPN services: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun setWireguardStatus(config: RouterConfig, start: Boolean) {
        val wgIface = config.wgInterface
        val command = if (start) "ifup $wgIface" else "ifdown $wgIface || ifdown wg0"
        sshClientManager.executeCommand(config, command)
    }

    private fun extractSectionName(line: String): String? {
        if (!line.contains("network.") || !line.contains("=")) return null
        val rawName = line.substringAfter("network.").substringBefore("=").trim()
        val sectionName = if (rawName.contains(".")) rawName.substringBefore(".") else rawName
        return if (sectionName.isNotEmpty()) sectionName else null
    }

    suspend fun setDns(config: RouterConfig, servers: List<String>) {
        val command = if (servers.isEmpty()) {
            // Revert to ISP DNS
            "uci set network.wan.peerdns='1'; uci del network.wan.dns 2>/dev/null; uci commit network; /etc/init.d/network restart"
        } else {
            // Set Custom DNS
            val addListCmd = servers.joinToString("; ") { "uci add_list network.wan.dns='$it'" }
            "uci set network.wan.peerdns='0'; uci del network.wan.dns 2>/dev/null; $addListCmd; uci commit network; /etc/init.d/network restart"
        }
        sshClientManager.executeCommand(config, command)
    }

    suspend fun fetchRouterInterfaces(config: RouterConfig): List<String> {
        val command = "uci show network | grep =interface; echo \"===DEVICE_CHECK===\"; uci show network | grep -E '.device=|.ifname='"
        return try {
            val res = sshClientManager.executeCommand(config, command)
            val stdout = res.stdout
            
            val parts = stdout.split("===DEVICE_CHECK===")
            val interfaceSection = parts.getOrNull(0).orEmpty()
            val deviceSection = parts.getOrNull(1).orEmpty()
            
            // 1. Запрос и извлечение имен интерфейсов (первый список)
            val firstListCandidates = mutableListOf<String>()
            val interfacesFromUciGrep = mutableSetOf<String>()
            
            interfaceSection.lines().forEach { line ->
                val trimmed = line.trim()
                val sectionName = extractSectionName(trimmed)
                if (sectionName != null) {
                    firstListCandidates.add(sectionName)
                    interfacesFromUciGrep.add(sectionName.lowercase())
                }
            }
            
            // 3. По команде uci show network | grep -E '.device=|.ifname=' получу второй список
            val tunExcludedSet = mutableSetOf<String>()
            deviceSection.lines().forEach { line ->
                val trimmed = line.trim()
                if (trimmed.contains("network.") && trimmed.contains(".")) {
                    val ifname = trimmed.substringAfter("network.").substringBefore(".").trim().lowercase()
                    if (trimmed.contains("device='")) {
                        val deviceVal = trimmed.substringAfter("device='").substringBefore("'")
                        // 4. В первом списке исключи интерфейсы по условию:
                        // имя интерфейса совпадает с именем во втором списке и название устройства содержит 'tun'
                        val containsTun = deviceVal.contains("tun") || trimmed.contains("device='tun") || trimmed.contains(".device='tun")
                        if (containsTun) {
                            tunExcludedSet.add(ifname)
                        }
                    }
                }
            }
            
            val excludedList = setOf("lan", "wan", "wan6", "loopback", "vpn_u")
            
            val finalResult = firstListCandidates.filter { candidate ->
                val lower = candidate.lowercase().trim()
                
                // - Из перечня автоматически исключаются системные интерфейсы lan, wan, wan6 и loopback
                if (lower in excludedList || lower.contains("vpn_u")) {
                    return@filter false
                }
                
                // - Исключается интерфейс wg0/wgo, если он отсутствует в выводе команды: uci show network | grep =interface
                if (lower == "wg0" || lower == "wgo") {
                    if (!interfacesFromUciGrep.contains(lower)) {
                        return@filter false
                    }
                }
                
                // - Исключение интерфейсов tun для OpenVPN
                if (lower in tunExcludedSet) {
                    return@filter false
                }
                
                true
            }
            
            finalResult.distinct().sorted()
        } catch (e: Exception) {
            Log.e("RouterRepository", "Failed to fetch router interfaces: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun fetchInterfacesWithProtos(config: RouterConfig): Map<String, String> {
        val command = "for dev in \$(uci show network 2>/dev/null | grep =interface | cut -d. -f2 | cut -d= -f1); do if [ -n \"\$dev\" ] && [ \"\$dev\" != \"lan\" ] && [ \"\$dev\" != \"wan\" ] && [ \"\$dev\" != \"wan6\" ] && [ \"\$dev\" != \"loopback\" ]; then echo \"\$dev:\$(uci -q get network.\$dev.proto)\"; fi; done"
        return try {
            val res = sshClientManager.executeCommand(config, command)
            val protoMap = mutableMapOf<String, String>()
            res.stdout.lines().forEach { line ->
                val trimmed = line.trim()
                if (trimmed.contains(":")) {
                    val label = trimmed.substringBefore(":")
                    val proto = trimmed.substringAfter(":")
                    if (label.isNotEmpty() && (proto == "wireguard" || proto == "amneziawg")) {
                        protoMap[label] = proto
                    }
                }
            }
            protoMap
        } catch (e: Exception) {
            Log.e("RouterRepository", "Failed to fetch interfaces with protocols: ${e.message}", e)
            emptyMap()
        }
    }

    suspend fun setLedStatus(config: RouterConfig, start: Boolean) {
        val command = if (hasUciTriggerError) {
            if (start) {
                "echo 1 > /sys/class/leds/blue:status/brightness"
            } else {
                "echo 0 > /sys/class/leds/blue:status/brightness"
            }
        } else {
            if (start) {
                val triggerToSet = if (!lastTriggerName.isNullOrBlank()) lastTriggerName else "default-on"
                "uci set system.@led[0].trigger='$triggerToSet' && uci commit system && /etc/init.d/led restart"
            } else {
                "uci set system.@led[0].trigger='none' && uci commit system && /etc/init.d/led restart"
            }
        }
        sshClientManager.executeCommand(config, command)
    }

    suspend fun rebootRouter(config: RouterConfig) {
        sshClientManager.executeCommand(config, "reboot")
    }

    suspend fun queryWanTrafficBytes(config: RouterConfig): TrafficAndTelemetry {
        val cmd = "route_iface=\$(ip route | grep '^default' | head -n1 | awk '{print \$5}'); if [ -n \"\$route_iface\" ] && [ -d \"/sys/class/net/\$route_iface\" ]; then cat /sys/class/net/\$route_iface/statistics/rx_bytes; cat /sys/class/net/\$route_iface/statistics/tx_bytes; else cat /proc/net/dev | grep -E \"(eth|wan|dsl|wlan|rmnet|ppp)\" | head -n1 | tr ':' ' ' | awk '{print \$2; print \$10}'; fi; echo '===CPU==='; top_out=\$(top -b -n 1 | grep -i 'cpu' | head -n 1); if [ -n \"\$top_out\" ]; then echo \"\$top_out\"; else cat /proc/loadavg 2>/dev/null; fi; echo '===MEM==='; cat /proc/meminfo 2>/dev/null | grep -E '^(MemTotal|MemFree|MemAvailable|Buffers|Cached):'; echo '===UPTIME==='; cat /proc/uptime 2>/dev/null"
        return try {
            val res = sshClientManager.executeCommand(config, cmd)
            val stdout = res.stdout

            val rxTxPart = extractSection(stdout, "", "===CPU===").ifEmpty {
                val index = stdout.indexOf("===CPU===")
                if (index != -1) stdout.substring(0, index) else stdout
            }
            val cpuPart = extractSection(stdout, "===CPU===", "===MEM===")
            val memPart = extractSection(stdout, "===MEM===", "===UPTIME===")
            val uptimePart = extractSection(stdout, "===UPTIME===", null)

            val rxTxLines = rxTxPart.lines().map { it.trim() }.filter { it.isNotEmpty() }
            val rx = rxTxLines.getOrNull(0)?.toLongOrNull() ?: 0L
            val tx = rxTxLines.getOrNull(1)?.toLongOrNull() ?: 0L

            val cpuUsage = parseCpuUsage(cpuPart)
            val memoryUsage = parseMemoryUsage(memPart.lines())
            val uptime = parseUptime(uptimePart)

            TrafficAndTelemetry(
                rxBytes = rx,
                txBytes = tx,
                cpuUsage = cpuUsage,
                memoryUsage = memoryUsage,
                uptime = uptime
            )
        } catch (e: Exception) {
            TrafficAndTelemetry()
        }
    }

    private fun parseCpuUsage(cpuLine: String): String {
        if (cpuLine.isEmpty()) return "—"
        val idleRegex = """(\d+(?:\.\d+)?)%\s*idle""".toRegex(RegexOption.IGNORE_CASE)
        val match = idleRegex.find(cpuLine)
        if (match != null) {
            val idleValue = match.groupValues[1].toFloatOrNull() ?: 100f
            val cpuVal = 100f - idleValue
            val clampedVal = if (cpuVal < 0f) 0f else if (cpuVal > 100f) 100f else cpuVal
            return String.format("%.1f%%", clampedVal)
        }

        val usrMatch = """(\d+(?:\.\d+)?)%\s*usr""".toRegex(RegexOption.IGNORE_CASE).find(cpuLine)
        val sysMatch = """(\d+(?:\.\d+)?)%\s*sys""".toRegex(RegexOption.IGNORE_CASE).find(cpuLine)
        if (usrMatch != null || sysMatch != null) {
            val usrVal = usrMatch?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
            val sysVal = sysMatch?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
            val sum = usrVal + sysVal
            val clampedVal = if (sum < 0f) 0f else if (sum > 100f) 100f else sum
            return String.format("%.1f%%", clampedVal)
        }

        val loadavgRegex = """\d+\.\d+""".toRegex()
        val loadMatch = loadavgRegex.findAll(cpuLine).toList()
        if (loadMatch.isNotEmpty()) {
            return loadMatch.first().value
        }

        return "—"
    }

    private fun parseMemoryUsage(memLines: List<String>): String {
        var memTotal = 0L
        var memFree = 0L
        var memAvailable = 0L
        var buffers = 0L
        var cached = 0L

        for (line in memLines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue
            val parts = trimmed.split("""\s+""".toRegex())
            if (parts.size >= 2) {
                val key = parts[0].removeSuffix(":").lowercase()
                val value = parts[1].toLongOrNull() ?: 0L
                when (key) {
                    "memtotal" -> memTotal = value
                    "memfree" -> memFree = value
                    "memavailable" -> memAvailable = value
                    "buffers" -> buffers = value
                    "cached" -> cached = value
                }
            }
        }

        if (memTotal <= 0L) return "—"

        // Calculate occupied (used) RAM. Prefer MemAvailable as it is more precise on modern kernel series.
        val used = if (memAvailable > 0L) {
            memTotal - memAvailable
        } else {
            memTotal - memFree - buffers - cached
        }
        val usedClamped = if (used < 0) 0L else used

        val usedMb = usedClamped / 1024.0
        val totalMb = memTotal / 1024.0
        val percent = (usedClamped.toDouble() / memTotal.toDouble()) * 100.0

        return String.format("%.1f МБ (%.0f%%)", usedMb, percent)
    }

    private fun parseUptime(uptimeLine: String): String {
        val secStr = uptimeLine.trim().split("""\s+""".toRegex()).firstOrNull() ?: return "—"
        val secondsTotal = secStr.toDoubleOrNull()?.toLong() ?: return "—"

        val days = secondsTotal / 86400
        val hours = (secondsTotal % 86400) / 3600
        val minutes = (secondsTotal % 3600) / 60

        return buildString {
            if (days > 0) append("${days}д ")
            if (hours > 0 || days > 0) append("${hours}ч ")
            append("${minutes}м")
        }
    }

    private fun parseIpInfo(json: String): Triple<String, String, String> {
        return try {
            val ipRegex = """"ip"\s*:\s*"([^"]+)"""".toRegex()
            val queryRegex = """"query"\s*:\s*"([^"]+)"""".toRegex()
            val cityRegex = """"city"\s*:\s*"([^"]+)"""".toRegex()
            val regionRegex = """"region"\s*:\s*"([^"]+)"""".toRegex()
            val countryRegex = """"country"\s*:\s*"([^"]+)"""".toRegex()
            val orgRegex = """"org"\s*:\s*"([^"]+)"""".toRegex()
            val ispRegex = """"isp"\s*:\s*"([^"]+)"""".toRegex()

            val ip = ipRegex.find(json)?.groupValues?.get(1)
                ?: queryRegex.find(json)?.groupValues?.get(1)
                ?: "Unknown"

            val city = cityRegex.find(json)?.groupValues?.get(1) ?: ""
            val region = regionRegex.find(json)?.groupValues?.get(1) ?: ""
            val country = countryRegex.find(json)?.groupValues?.get(1) ?: ""

            val org = orgRegex.find(json)?.groupValues?.get(1)
                ?: ispRegex.find(json)?.groupValues?.get(1)
                ?: "Unknown"

            val location = listOf(city, region, country).filter { it.isNotEmpty() }.joinToString(", ")
            Triple(ip, if (location.isEmpty()) "Unknown" else location, org)
        } catch (e: Exception) {
            Triple("Unknown", "Unknown", "Unknown")
        }
    }

    suspend fun disconnect() {
        sshClientManager.disconnect()
    }
}
