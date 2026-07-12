import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_arp = """                    if (dev.startsWith("br-") || dev.startsWith("eth") || dev.startsWith("lan")) {
                        activeMacs.add(mac)
                        macToInterface[mac] = dev
                        if (!macToIp.containsKey(mac)) {
                            macToIp[mac] = ip
                        }
                    }"""

new_arp = """                    activeMacs.add(mac)
                    macToInterface[mac] = dev
                    if (!macToIp.containsKey(mac)) {
                        macToIp[mac] = ip
                    }"""

content = content.replace(old_arp, new_arp)

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)
