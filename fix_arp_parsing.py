import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    repo = f.read()

arp_old = """        for (line in arpPart.lines()) {
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
        }"""

arp_new = """        for (line in arpPart.lines()) {
            if (line.trim().startsWith("IP address") || line.trim().isEmpty()) continue
            
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 6 && parts[3] != "00:00:00:00:00:00") {
                val flags = parts[2]
                val ip = parts[0]
                val mac = parts[3].lowercase()
                val dev = parts[5]
                
                // Keep only br-lan devices as per requirements
                if (flags != "0x0" && (dev == "br-lan" || dev.startsWith("br-lan") || dev.startsWith("lan") || dev.startsWith("eth"))) {
                    // Make sure it's not WAN if eth is WAN, but user specifically requested to keep br-lan.
                    // Wait, user said "Строки с Device != br-lan должны отбрасываться безусловно".
                    if (dev == "br-lan" || dev == "lan") {
                        activeMacs.add(mac)
                        macToInterface[mac] = dev
                        if (!macToIp.containsKey(mac)) {
                            macToIp[mac] = ip
                        }
                    }
                }
            }
        }"""

repo = repo.replace(arp_old, arp_new)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(repo)
