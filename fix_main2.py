with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('Text("IP: ${device.ip}"', 'Text("${stringResource(R.string.label_ip_colon)}${device.ip}"')
content = content.replace('Text("MAC: ${device.mac}"', 'Text("${stringResource(R.string.label_mac_colon)}${device.mac}"')

content = content.replace('Text("—"', 'Text("—"')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
