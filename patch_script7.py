import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = r"view\.onProcessIncomingText\(\"\"\)"
replacement = r'view.onProcessIncomingText("\r")'

new_content = re.sub(target, replacement, content)
if content != new_content:
    with open(file_path, "w") as f:
        f.write(new_content)
    print("Replaced successfully")
else:
    print("Not replaced")
