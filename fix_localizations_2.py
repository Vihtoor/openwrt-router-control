import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Let's find getDialogTitle and getDialogBody using exact matching and insert correctly.
# In getDialogTitle, we have "iperf" -> ...
# We'll insert after iperf

iperf_title_idx = content.find('"iperf" -> when')
iperf_title_end = content.find('            }', iperf_title_idx) + 13

devices_title = """            "devices" -> when (normalizedLang) {
                "ru" -> "Устройства"
                "uk" -> "Пристрої"
                "be" -> "Прылады"
                else -> "Devices"
            }
"""

content = content[:iperf_title_end] + "\n" + devices_title + content[iperf_title_end:]


iperf_body_idx = content.find('"iperf" -> when', iperf_title_end)
iperf_body_end = content.find('            }', iperf_body_idx) + 13

devices_body = """            "devices" -> when (normalizedLang) {
                "ru" -> "**Список устройств:**\\nЭтот инструмент отображает список всех устройств, подключенных к вашему роутеру. Вы можете видеть их IP и MAC адреса, тип подключения (Wi-Fi или Ethernet), а также статистику потребляемого трафика и текущую скорость.\\n\\n**Важное замечание про Ethernet:**\\nДля корректного определения устройств, подключенных по кабелю, и отображения достоверной информации об их трафике, на роутере должны быть установлены специальные утилиты:\\n• Если роутер имеет архитектуру с мостом (bridge), необходима утилита **bridge**.\\n• Если используется архитектура со свитчом (switch), необходима утилита **swconfig**.\\nБез этих утилит информация о трафике проводных устройств может отображаться некорректно (например, может выводиться общий трафик всего физического порта роутера, а не отдельного устройства)."
                else -> "**Device List:**\\nThis tool displays a list of all devices connected to your router. You can see their IP and MAC addresses, connection type (Wi-Fi or Ethernet), as well as traffic consumption statistics and current speed.\\n\\n**Important note about Ethernet:**\\nTo correctly detect wired devices and display accurate traffic information, special utilities must be installed on the router:\\n• For a bridge architecture, the **bridge** utility is required.\\n• For a switch architecture, the **swconfig** utility is required.\\nWithout these utilities, traffic information for wired devices may be incorrect (e.g., showing total traffic for the router's physical port rather than the specific device)."
            }
"""

content = content[:iperf_body_end] + "\n" + devices_body + content[iperf_body_end:]

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)

print("Done inserting properly")
