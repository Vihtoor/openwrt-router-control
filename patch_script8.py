import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = 'view.onProcessIncomingText("")'
replacement = 'view.onProcessIncomingText("\\r")'

content = content.replace(target, replacement)
with open(file_path, "w") as f:
    f.write(content)
print("Done")
