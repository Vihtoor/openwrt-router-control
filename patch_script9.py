import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = 'view.onProcessIncomingText("  \n")'
replacement = 'view.onProcessIncomingText("\\r")'

content = content.replace('view.onProcessIncomingText("\n")', 'view.onProcessIncomingText("\\r")')

with open(file_path, "w") as f:
    f.write(content)
print("Done")
