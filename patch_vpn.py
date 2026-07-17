import re

file_path = "app/src/main/java/com/example/ui/RouterViewModel.kt"

with open(file_path, "r") as f:
    content = f.read()

target1 = """                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower ||
                        (freshStatus.openVpnInstanceName.isNullOrEmpty() && config.openVpnService == serviceName)
                    )"""

replacement1 = """                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower
                    )"""

content = content.replace(target1, replacement1)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching vpn")
