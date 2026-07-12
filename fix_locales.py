import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Fix getDialogTitle devices
old_devices_title = """            "devices" -> when (normalizedLang) {
                "ru" -> "Устройства"
                "uk" -> "Пристрої"
                "be" -> "Прылады"
                else -> "Devices"
            }"""

new_devices_title = """            "devices" -> when (normalizedLang) {
                "ru" -> "Устройства, подключенные к роутеру"
                "uk" -> "Пристрої, підключені до маршрутизатора"
                "be" -> "Прылады, падлучаныя да маршрутызатара"
                "de" -> "Mit dem Router verbundene Geräte"
                "es" -> "Dispositivos conectados al enrutador"
                "fr" -> "Appareils connectés au routeur"
                "it" -> "Dispositivi connessi al router"
                "pt" -> "Dispositivos conectados ao roteador"
                "da" -> "Enheder tilsluttet routeren"
                "fi" -> "Reitittimeen yhdistetyt laitteet"
                "kk" -> "Маршрутизаторға қосылған құрылғылар"
                "lt" -> "Prie maršrutizatoriaus prijungti įrenginiai"
                "lv" -> "Maršrutētājam pievienotās ierīces"
                "sv" -> "Enheter anslutna till routern"
                else -> "Devices connected to the router"
            }"""

content = content.replace(old_devices_title, new_devices_title)

# Remove the broken devices body inside getDialogTitle
broken_devices_body_pattern = r'            "devices" -> when \(normalizedLang\) \{\s+"ru" -> "\*\*Список устройств:\*\*.*?\n\n\*\*Важное замечание про Ethernet:\*\*.*?\n• Если роутер имеет архитектуру.*?\n• Если используется архитектура.*?\nБез этих утилит.*?"\s+else -> "\*\*Device List:\*\*.*?"\s+\}'
content = re.sub(broken_devices_body_pattern, '', content, flags=re.DOTALL)

# Now, we need to add the proper devices block into getDialogBody
# Find where the `getDialogBody` when block is
body_block_pattern = r'    fun getDialogBody\(key: String, lang: String, config: com.example.data.RouterConfig\? = null\): String \{\s+val normalizedLang = when \(lang\) \{\s+"ru".*?-> lang\s+else -> "en"\s+\}\s+return when \(key\) \{'

# Ensure the body block has the correct dynamic code for devices
dynamic_body = """
            "devices" -> {
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
                
                val installInstructionRu = "\\n\\nДля установки необходимой утилиты скопируйте эту команду и вставьте её в консоль:\\n`opkg update && opkg install $reqUtil`"
                val installInstructionEn = "\\n\\nTo install the necessary utility, copy this command and paste it into the console:\\n`opkg update && opkg install $reqUtil`"
                
                val installText = if (!hasUtil && config?.capabilities?.switchArchitecture != com.example.data.SwitchArchitecture.UNSUPPORTED && config?.capabilities?.switchArchitecture != null) {
                    if (lang == "ru") installInstructionRu else installInstructionEn
                } else ""
                
                val ethernetNoteRu = "**Важное замечание про Ethernet:**\\nВаш роутер имеет: $archStr. Для возможности определения устройств, подключенных к роутеру через Ethernet, и вывода достоверной информации об их трафике необходима утилита **$reqUtil**.$utilStatus$installText"
                val ethernetNoteEn = "**Important note about Ethernet:**\\nYour router has: $archStr. To detect devices connected to the router via Ethernet and display accurate traffic information, the **$reqUtil** utility is required.$utilStatus$installText"
                
                when (normalizedLang) {
                    "ru" -> "**Список устройств:**\\nЭтот инструмент отображает список всех устройств, подключенных к вашему роутеру. Вы можете видеть их IP и MAC адреса, тип подключения (Wi-Fi или Ethernet), а также статистику потребляемого трафика и текущую скорость.\\n\\n$ethernetNoteRu"
                    else -> "**Device List:**\\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\\n\\n$ethernetNoteEn"
                }
            }"""

# Insert `dynamic_body` right after `return when (key) {`
content = re.sub(body_block_pattern, lambda m: m.group(0) + dynamic_body, content)

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
print("done")
