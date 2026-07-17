import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

# Find all pendingEraseSkips declarations
lines = content.split('\n')
new_lines = []
skip_count = 0
for line in lines:
    if "private var pendingEraseSkips = 0" in line:
        skip_count += 1
        if skip_count > 1:
            continue
    new_lines.append(line)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write('\n'.join(new_lines))
