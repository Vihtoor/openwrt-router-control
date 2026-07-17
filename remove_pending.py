import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("pendingEraseSkips = 0\n", "")
content = content.replace("                pendingEraseSkips = 0\n", "")

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
