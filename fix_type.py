import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_type = """                Text("Тип: ${device.connectionType}", style = MaterialTheme.typography.bodyMedium)"""
new_type = """                Text(device.connectionType, style = MaterialTheme.typography.bodyMedium)"""

if old_type in content:
    content = content.replace(old_type, new_type)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Type replaced")
else:
    print("Type not found")
