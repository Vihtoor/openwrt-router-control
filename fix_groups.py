import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# For lines 9420-9430 and 9750-9765:
replacement_1 = """
    val strWifi6 = stringResource(R.string.wifi_6_ghz)
    val strWifi5 = stringResource(R.string.wifi_5_ghz)
    val strWifi24 = stringResource(R.string.wifi_24_ghz)
    val strWifiOther = stringResource(R.string.wifi_other)
    val strEthernet = stringResource(R.string.ethernet)
    
    val groups = listOf(
        strWifi6 to sortedDevices.filter { it.connectionType == strWifi6 },
        strWifi5 to sortedDevices.filter { it.connectionType == strWifi5 },
        strWifi24 to sortedDevices.filter { it.connectionType == strWifi24 },
        strWifiOther to sortedDevices.filter { it.connectionType.contains("Wi-Fi") && it.connectionType != strWifi6 && it.connectionType != strWifi5 && it.connectionType != strWifi24 },
        strEthernet to sortedDevices.filter { it.connectionType == strEthernet }
    )
"""

content = re.sub(
    r'val groups = listOf\(\s*stringResource\(R.string.wifi_6_ghz\).*?stringResource\(R.string.ethernet\) to sortedDevices.filter \{ !it.connectionType.contains\("Wi-Fi"\) \}\s*\)',
    replacement_1.strip(),
    content,
    flags=re.DOTALL
)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
