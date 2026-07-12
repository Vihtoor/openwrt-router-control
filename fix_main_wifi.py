with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('it.connectionType.contains("6 ГГц")', 'it.connectionType == stringResource(R.string.wifi_6_ghz)')
content = content.replace('it.connectionType.contains("5 ГГц")', 'it.connectionType == stringResource(R.string.wifi_5_ghz)')
content = content.replace('it.connectionType.contains("2.4 ГГц")', 'it.connectionType == stringResource(R.string.wifi_24_ghz)')
content = content.replace('it.connectionType.contains("Wi-Fi") && !it.connectionType.contains("ГГц")', 'it.connectionType.contains("Wi-Fi") && it.connectionType != stringResource(R.string.wifi_6_ghz) && it.connectionType != stringResource(R.string.wifi_5_ghz) && it.connectionType != stringResource(R.string.wifi_24_ghz)')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
