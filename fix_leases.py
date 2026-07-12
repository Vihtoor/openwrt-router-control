import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    repo = f.read()

old = """        for (line in leasesPart.lines()) {
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
            if (line.trim().startsWith("IP address") || line.trim().isEmpty()) continue
            
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 6 && parts[3] != "00:00:00:00:00:00") {
                val flags = parts[2]
                val ip = parts[0]
                val mac = parts[3].lowercase()
                val dev = parts.last()
                
                if (flags != "0x0" && dev == "br-lan") {
                    activeMacs.add(mac)
                    macToInterface[mac] = dev
                    if (!macToIp.containsKey(mac)) {
                        macToIp[mac] = ip
                    }
                }
            }
        }"""

new = """        for (line in leasesPart.lines()) {
            val parts = line.trim().split(\"\"\"\\s+\"\"\".toRegex())
            if (parts.size >= 4) {
                val mac = parts[1].lowercase()
                val ip = parts[2]
                val hostname = parts[3].takeIf { it != "*" } ?: "Unknown"
                macToHostname[mac] = hostname
                macToIp[mac] = ip
                
                activeMacs.add(mac)
                macToInterface[mac] = "br-lan"
            }
        }
        
        for (line in arpPart.lines()) {
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

repo = repo.replace(old, new)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(repo)
