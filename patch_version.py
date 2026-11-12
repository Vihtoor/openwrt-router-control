import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

content = content.replace('versionName = "1.6.8"', 'versionName = "1.6.9"')
content = content.replace('versionCode = 9', 'versionCode = 10')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
