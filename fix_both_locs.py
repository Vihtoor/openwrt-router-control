import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Restore getDialogTitle's "devices" block
old_devices_in_title = """            "devices" -> {
                val archStr = when (config?.capabilities?.switchArchitecture) {"""

if old_devices_in_title in content:
    # Find start and end of this bad block in title
    start = content.find('            "devices" -> {')
    end = content.find('            }', start) + 13
    
    # Actually wait, there is another when (normalizedLang) inside it
    # Let's just find the end manually:
    
    end = content.find('                }\n            }', start) + 31

    good_title = """            "devices" -> when (normalizedLang) {
                "ru" -> "Устройства"
                "uk" -> "Пристрої"
                "be" -> "Прылады"
                else -> "Devices"
            }"""
            
    content = content[:start] + good_title + content[end:]
    print("Fixed title")


# Now add the dynamic block into getDialogBody
# Find "devices" -> when (normalizedLang) { inside getDialogBody
body_start = content.find('fun getDialogBody')
devices_in_body_start = content.find('            "devices" -> when (normalizedLang) {', body_start)

if devices_in_body_start != -1:
    end = content.find('            }', devices_in_body_start) + 13
    
    dynamic_body = """            "devices" -> {
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
    
    content = content[:devices_in_body_start] + dynamic_body + content[end:]
    print("Fixed body")

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
