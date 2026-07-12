import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_sort = """                        val sortedDevices = if (isDownload) {
                            devices.sortedByDescending { it.downloadSpeedMbps }
                        } else {
                            devices.sortedByDescending { it.uploadSpeedMbps }
                        }"""

new_sort = """                        val sortedDevices = devices.sortedByDescending { it.downloadBytes }"""

if old_sort in content:
    content = content.replace(old_sort, new_sort)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Sort Success")
else:
    print("Sort Not found")
