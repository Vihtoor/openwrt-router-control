import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

# Find all pendingEraseSkips declarations and usages
lines = content.split('\n')
new_lines = []
for line in lines:
    if "private var pendingEraseSkips" in line:
        continue
    new_lines.append(line)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write('\n'.join(new_lines))
