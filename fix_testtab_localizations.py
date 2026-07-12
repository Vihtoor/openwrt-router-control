import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Replace $reqUtil with ip-bridge or swconfig depending on architecture!
# Wait, reqUtil is defined as:
# val reqUtil = when (config?.capabilities?.switchArchitecture) { ... }
# In the instructions, the user wants us to change `bridge` to `ip-bridge` in the command.
# Let's modify the installInstruction texts to use a variable `installCmd` instead of hardcoding `opkg update && opkg install $reqUtil`.
# Wait, the user specifically says: "Исправь команду установки утилиты bridge на: opkg update && opkg install ip-bridge "

# Let's check how reqUtil is defined:
req_util_pattern = r'val reqUtil = when \(config\?\.capabilities\?\.switchArchitecture\) \{.*?\n\s+\}'
req_util_replacement = """val reqUtil = when (config?.capabilities?.switchArchitecture) {
                    com.example.data.SwitchArchitecture.DSA -> "ip-bridge"
                    com.example.data.SwitchArchitecture.SWCONFIG -> "swconfig"
                    else -> when (normalizedLang) {
                        "ru" -> "ip-bridge или swconfig"
                        "uk" -> "ip-bridge або swconfig"
                        "be" -> "ip-bridge або swconfig"
                        "de" -> "ip-bridge oder swconfig"
                        "es" -> "ip-bridge o swconfig"
                        "fr" -> "ip-bridge ou swconfig"
                        "it" -> "ip-bridge o swconfig"
                        "pt" -> "ip-bridge ou swconfig"
                        "da" -> "ip-bridge eller swconfig"
                        "fi" -> "ip-bridge tai swconfig"
                        "kk" -> "ip-bridge немесе swconfig"
                        "lt" -> "ip-bridge arba swconfig"
                        "lv" -> "ip-bridge vai swconfig"
                        "sv" -> "ip-bridge eller swconfig"
                        else -> "ip-bridge or swconfig"
                    }
                }"""

# Wait, if we change reqUtil to "ip-bridge", then it will also say "Утилита ip-bridge не найдена..." which is correct.

content = re.sub(req_util_pattern, req_util_replacement, content, flags=re.DOTALL)

# Next, remove backticks from installInstruction:
content = content.replace("`opkg update && opkg install $reqUtil`", "opkg update && opkg install $reqUtil")

# Next, add the new sentence at the end of the ethernetNote or the main device list description!
# "В  конце описания карточки "Устройства" добавь, что список устройств также можно отобразить нажав на "Загрузка" и "Выгрузка" на графике трафика и загрузки процессора на главном экране приложения."
# This is added to the getDialogBody's final when block for "devices".
# Let's do that.

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
print("done")
