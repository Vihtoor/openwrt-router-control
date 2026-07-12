import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

content = content.replace("nlbw -c csv -g mac 2>/dev/null", "nlbw -c csv -g mac,date 2>/dev/null | grep -E \\\"(mac|\\$(date +%F))\\\"")

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("${formatBytes(rightPaneDevice.downloadBytes)}", "${formatBytes(rightPaneDevice.downloadMonthBytes)}")
content = content.replace("${formatBytes(rightPaneDevice.uploadBytes)}", "${formatBytes(rightPaneDevice.uploadMonthBytes)}")

content = content.replace("${formatBytes(currentDevice.downloadBytes)}", "${formatBytes(currentDevice.downloadMonthBytes)}")
content = content.replace("${formatBytes(currentDevice.uploadBytes)}", "${formatBytes(currentDevice.uploadMonthBytes)}")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
