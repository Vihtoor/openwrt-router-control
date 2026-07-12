with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('if (groupName == stringResource(R.string.ethernet) && devicesInGroup.isEmpty()) {', 'if (groupName == strEthernet && devicesInGroup.isEmpty()) {')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
