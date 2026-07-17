import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                update = { view ->
                    view.shouldRequestFocusOnWindowFocus = !showConsoleInteractiveTip
                    view.onProcessIncomingText = { insertedText ->"""

replacement = """                update = { view ->
                    view.shouldRequestFocusOnWindowFocus = !showConsoleInteractiveTip
                    view.isFocusable = !showConsoleInteractiveTip
                    view.isFocusableInTouchMode = !showConsoleInteractiveTip
                    if (showConsoleInteractiveTip) view.clearFocus()
                    view.onProcessIncomingText = { insertedText ->"""

content = content.replace(target, replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching terminal view usage 2")
