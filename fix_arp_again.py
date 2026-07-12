import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    repo = f.read()

old = """        for (line in arpPart.lines()) {
            if (line.trim().startsWith("IP address") || line.trim().isEmpty()) continue
            
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 6 && parts[3] != "00:00:00:00:00:00") {
                val flags = parts[2]
                val ip = parts[0]
                val mac = parts[3].lowercase()
                val dev = parts.last()
                
                if (dev == "br-lan") {
                    activeMacs.add(mac)
                    macToInterface[mac] = dev
                    if (!macToIp.containsKey(mac) || flags != "0x0") {
                        macToIp[mac] = ip
                    }
                }
            }
        }"""

new = """        for (line in arpPart.lines()) {
            if (line.trim().startsWith("IP address") || line.trim().isEmpty()) continue
            
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 6 && parts[3] != "00:00:00:00:00:00") {
                val flags = parts[2]
                val ip = parts[0]
                val mac = parts[3].lowercase()
                val dev = parts.last()
                
                if (dev == "br-lan") {
                    if (activeMacs.contains(mac)) {
                        macToInterface[mac] = dev
                        // Update IP if we have a fresh ARP entry (0x2)
                        if (flags != "0x0") {
                            macToIp[mac] = ip
                        }
                    }
                }
            }
        }"""

repo = repo.replace(old, new)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(repo)
