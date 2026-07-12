import re

with open('app/src/main/java/com/example/data/RouterRepository.kt', 'r') as f:
    content = f.read()

content = content.replace('var band = context.getString(com.example.R.string.wifi_other).replace("(Другое)", "($iface)")', 'var band = "Wi-Fi ($iface)"')

with open('app/src/main/java/com/example/data/RouterRepository.kt', 'w') as f:
    f.write(content)
