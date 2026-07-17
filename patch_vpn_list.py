import re

file_path = "app/src/main/java/com/example/ui/RouterViewModel.kt"
with open(file_path, "r") as f:
    content = f.read()

# Remove the explicit additions
target1 = """                if (freshStatus.isOpenVpnActive) {
                    for (activeOvpn in activeOvpnList) {
                        if (!addedOvpn.contains(activeOvpn)) {
                            newVpnList.add(RouterVpnItem(name = activeOvpn, type = "OpenVPN", isRunning = true, isChecked = true))
                        }
                    }
                }"""

target2 = """                for (activeWg in activeWgList) {
                    if (!addedWg.contains(activeWg)) {
                        newVpnList.add(RouterVpnItem(name = activeWg, type = "WireGuard", isRunning = true, isChecked = true))
                    }
                }"""

count1 = content.count(target1)
count2 = content.count(target2)

content = content.replace(target1, "")
content = content.replace(target2, "")

with open(file_path, "w") as f:
    f.write(content)

print(f"Patched target1: {count1}, target2: {count2}")
