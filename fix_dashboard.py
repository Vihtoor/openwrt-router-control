import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    if "            item {" in line and "            }" in lines[i+1] and "SpeedMeterCard" not in lines[i+1]:
        # we found an empty item { } where SpeedMeterCard used to be
        new_lines.append(line)
        new_lines.append("                SpeedMeterCard(state = state, onNavigateToConsole = onNavigateToConsole)\n")
    else:
        new_lines.append(line)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.writelines(new_lines)
