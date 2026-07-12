import re

with open('app/src/main/java/com/example/data/RouterRepository.kt', 'r') as f:
    content = f.read()

content = content.replace('var band = "Wi-Fi ($iface)"', 'var band = context.getString(com.example.R.string.wifi_other).replace("(Другое)", "($iface)")')
content = content.replace('if (freq in 2400..2500) band = "Wi-Fi 2.4 ГГц"', 'if (freq in 2400..2500) band = context.getString(com.example.R.string.wifi_24_ghz)')
content = content.replace('else if (freq in 5000..5900) band = "Wi-Fi 5 ГГц"', 'else if (freq in 5000..5900) band = context.getString(com.example.R.string.wifi_5_ghz)')
content = content.replace('else if (freq in 5900..7200) band = "Wi-Fi 6 ГГц"', 'else if (freq in 5900..7200) band = context.getString(com.example.R.string.wifi_6_ghz)')

with open('app/src/main/java/com/example/data/RouterRepository.kt', 'w') as f:
    f.write(content)
