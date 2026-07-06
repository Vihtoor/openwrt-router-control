import re

with open("app/src/main/java/com/example/RouterWidgetProvider.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Fix for OpenVPN in first fetch (around line 121)
old_ovpn_1 = """                    lastOpenVpnText = if (status.isOpenVpnActive) {
                        if (!status.openVpnInstanceName.isNullOrEmpty()) "Запущена (${status.openVpnInstanceName})" else "Запущена"
                    } else {"""
new_ovpn_1 = """                    lastOpenVpnText = if (status.isOpenVpnActive) {
                        if (!status.activeOpenVpnInstances.isNullOrEmpty()) "Запущена (${status.activeOpenVpnInstances})"
                        else if (!status.openVpnInstanceName.isNullOrEmpty()) "Запущена (${status.openVpnInstanceName})" 
                        else "Запущена"
                    } else {"""
content = content.replace(old_ovpn_1, new_ovpn_1)


# Fix for OpenVPN in second fetch (around line 253)
old_ovpn_2 = """                    lastOpenVpnText = if (resultStatus.isOpenVpnActive) {
                        if (!resultStatus.openVpnInstanceName.isNullOrEmpty()) "Запущена (${resultStatus.openVpnInstanceName})" else "Запущена"
                    } else {"""
new_ovpn_2 = """                    lastOpenVpnText = if (resultStatus.isOpenVpnActive) {
                        if (!resultStatus.activeOpenVpnInstances.isNullOrEmpty()) "Запущена (${resultStatus.activeOpenVpnInstances})"
                        else if (!resultStatus.openVpnInstanceName.isNullOrEmpty()) "Запущена (${resultStatus.openVpnInstanceName})" 
                        else "Запущена"
                    } else {"""
content = content.replace(old_ovpn_2, new_ovpn_2)

with open("app/src/main/java/com/example/RouterWidgetProvider.kt", "w", encoding="utf-8") as f:
    f.write(content)
