import re

file_path = "app/src/main/java/com/example/ui/RouterViewModel.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                    pendingCommand?.let { cmd ->
                        pendingCommand = null
                        when (cmd) {
                            "led_on" -> toggleLed(true)
                            "led_off" -> toggleLed(false)
                            "reboot" -> rebootRouter()
                            "vpn_on" -> toggleMasterVpn(true)
                            "vpn_off" -> toggleMasterVpn(false)
                        }
                    }"""

replacement = """                    pendingCommand?.let { cmd ->
                        pendingCommand = null
                        when (cmd) {
                            "led_on" -> toggleLed(true)
                            "led_off" -> toggleLed(false)
                            "reboot" -> rebootRouter()
                            "vpn_on" -> toggleMasterVpn(true)
                            "vpn_off" -> toggleMasterVpn(false)
                        }
                    }
                    if (isFirstEmission && _uiState.value.currentTab == TabType.CONSOLE && interactiveShellJob == null) {
                        startInteractiveShellSession()
                    }"""

content = content.replace(target, replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching init")
