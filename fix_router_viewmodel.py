import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Fix for the first block (around line 714)
old_block_1 = """                // 1. OpenVPN services
                for (serviceName in availableOvpn) {
                    val sNameLower = serviceName.lowercase()
                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower ||
                        (freshStatus.openVpnInstanceName.isNullOrEmpty() && config.openVpnService == serviceName)
                    )
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = serviceName, type = "OpenVPN", isRunning = isRunning, isChecked = isChecked))
                }

                // 2. Interfaces (WireGuard and AmneziaWG)
                for ((ifaceName, proto) in ifacesWithProtos) {
                    val type = if (proto == "amneziawg") "AmneziaWG" else "WireGuard"
                    val isRunning = activeWgList.contains(ifaceName)
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = ifaceName, type = type, isRunning = isRunning, isChecked = isChecked))
                }"""

new_block_1 = """                // 1. OpenVPN services
                val addedOvpn = mutableSetOf<String>()
                for (serviceName in availableOvpn) {
                    val sNameLower = serviceName.lowercase()
                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower ||
                        (freshStatus.openVpnInstanceName.isNullOrEmpty() && config.openVpnService == serviceName)
                    )
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = serviceName, type = "OpenVPN", isRunning = isRunning, isChecked = isChecked))
                    addedOvpn.add(sNameLower)
                }
                
                if (freshStatus.isOpenVpnActive) {
                    for (activeOvpn in activeOvpnList) {
                        if (!addedOvpn.contains(activeOvpn)) {
                            newVpnList.add(RouterVpnItem(name = activeOvpn, type = "OpenVPN", isRunning = true, isChecked = true))
                        }
                    }
                }

                // 2. Interfaces (WireGuard and AmneziaWG)
                val addedWg = mutableSetOf<String>()
                for ((ifaceName, proto) in ifacesWithProtos) {
                    val type = if (proto == "amneziawg") "AmneziaWG" else "WireGuard"
                    val isRunning = activeWgList.contains(ifaceName)
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = ifaceName, type = type, isRunning = isRunning, isChecked = isChecked))
                    addedWg.add(ifaceName)
                }
                
                for (activeWg in activeWgList) {
                    if (!addedWg.contains(activeWg)) {
                        newVpnList.add(RouterVpnItem(name = activeWg, type = "WireGuard", isRunning = true, isChecked = true))
                    }
                }"""

# Fix for the second block (around line 800)
# It's exactly the same content, so we can just replace all occurrences.

content = content.replace(old_block_1, new_block_1)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w", encoding="utf-8") as f:
    f.write(content)
