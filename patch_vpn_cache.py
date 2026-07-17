import re

file_path = "app/src/main/java/com/example/ui/RouterViewModel.kt"
with open(file_path, "r") as f:
    content = f.read()

# Fix OpenVPN cache usage
target1 = """                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower
                    )"""
replacement1 = """                    val isRunning = freshStatus.isOpenVpnActive && activeOvpnList.contains(sNameLower)"""

if target1 in content:
    content = content.replace(target1, replacement1)
    print("Patched target1")
else:
    print("target1 not found")

with open(file_path, "w") as f:
    f.write(content)
