import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Change signature
content = content.replace("fun getDialogBody(dialog: String, lang: String): String", "fun getDialogBody(dialog: String, lang: String, config: com.example.data.RouterConfig? = null): String")

# Now rewrite the "devices" block in getDialogBody to use config
new_devices_block = """            "devices" -> {
                val archStr = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> if (lang == "ru") "архитектура моста (DSA)" else "bridge architecture (DSA)"
                    com.example.data.SwitchArchitecture.SWCONFIG -> if (lang == "ru") "архитектура со свитчом (SWCONFIG)" else "switch architecture (SWCONFIG)"
                    com.example.data.SwitchArchitecture.UNSUPPORTED -> if (lang == "ru") "неподдерживаемая архитектура" else "unsupported architecture"
                    else -> if (lang == "ru") "неизвестная архитектура" else "unknown architecture"
                }
                
                val reqUtil = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> "bridge"
                    com.example.data.SwitchArchitecture.SWCONFIG -> "swconfig"
                    else -> if (lang == "ru") "bridge или swconfig" else "bridge or swconfig"
                }
                
                val hasUtil = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> config?.capabilities?.hasBridgeUtil == true
                    com.example.data.SwitchArchitecture.SWCONFIG -> config?.capabilities?.hasSwconfigUtil == true
                    else -> false
                }
                
                val utilStatus = if (hasUtil) {
                    if (lang == "ru") "\\nУтилита **$reqUtil** установлена в системе, всё работает корректно." else "\\nThe **$reqUtil** utility is installed in the system, everything works correctly."
                } else {
                    if (lang == "ru") "\\nУтилита **$reqUtil** не найдена в системе! Информация о трафике проводных устройств может отображаться некорректно." else "\\nThe **$reqUtil** utility is not found in the system! Traffic information for wired devices may be incorrect."
                }
                
                val ethernetNoteRu = "**Важное замечание про Ethernet:**\\nВаш роутер имеет: $archStr. Для возможности определения устройств, подключенных к роутеру через Ethernet, и вывода достоверной информации об их трафике необходима утилита **$reqUtil**.$utilStatus"
                val ethernetNoteEn = "**Important note about Ethernet:**\\nYour router has: $archStr. To detect devices connected to the router via Ethernet and display accurate traffic information, the **$reqUtil** utility is required.$utilStatus"
                
                when (normalizedLang) {
                    "ru" -> "**Список устройств:**\\nЭтот инструмент отображает список всех устройств, подключенных к вашему роутеру. Вы можете видеть их IP и MAC адреса, тип подключения (Wi-Fi или Ethernet), а также статистику потребляемого трафика и текущую скорость.\\n\\n$ethernetNoteRu"
                    else -> "**Device List:**\\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\\n\\n$ethernetNoteEn"
                }
            }"""

# Replace existing "devices" -> when (normalizedLang) { ... }
start_idx = content.find('"devices" -> when (normalizedLang) {')
end_idx = content.find('            }', start_idx) + 13

content = content[:start_idx] + new_devices_block + content[end_idx:]

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
print("done")
