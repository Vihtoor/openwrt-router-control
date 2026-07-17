import re

file_path = "app/src/main/java/com/example/data/RouterRepository.kt"

with open(file_path, "r") as f:
    content = f.read()

target1 = """            // Parse OpenVPN
            val isOpenVpnActive = openVpnPart.lines().any { line ->
                isValidOpenVpnProcessLine(line)
            }
            val vpnInstanceName = if (isOpenVpnActive) extractOpenVpnInstanceName(openVpnPart) else null
            val activeOpenVpnNamesSet = if (isOpenVpnActive) extractAllOpenVpnInstanceNames(openVpnPart) else emptySet()
            val activeOpenVpnNamesStr = activeOpenVpnNamesSet.joinToString(",")"""

replacement1 = """            // Parse OpenVPN
            val activeOpenVpnNamesSet = extractAllOpenVpnInstanceNames(openVpnPart)
            val isOpenVpnActive = activeOpenVpnNamesSet.isNotEmpty()
            val vpnInstanceName = activeOpenVpnNamesSet.firstOrNull()
            val activeOpenVpnNamesStr = activeOpenVpnNamesSet.joinToString(",")"""

content = content.replace(target1, replacement1)

target2 = """                val openVpnCheck = try {
                    val res = sshClientManager.executeCommand(config, "ps -w | grep '[o]penvpn'")
                    res.stdout.lines().any { line ->
                        isValidOpenVpnProcessLine(line)
                    }
                } catch (ex: Exception) { false }"""

replacement2 = """                val openVpnCheck = try {
                    val res = sshClientManager.executeCommand(config, "ps -w | grep '[o]penvpn'")
                    extractAllOpenVpnInstanceNames(res.stdout).isNotEmpty()
                } catch (ex: Exception) { false }"""

content = content.replace(target2, replacement2)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching repo")
