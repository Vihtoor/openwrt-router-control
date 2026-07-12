with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    # Just write out logic to clean up the bad insertion in ConsoleTab
    if "if (showDevices) {" in line and i > 3900 and i < 4000:
        continue # we will skip this and next 8 lines
    # etc...
