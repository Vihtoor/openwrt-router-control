import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {"""

replacement = """                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_CHANNEL_UP) {
                                onWriteRawToConsoleStdin("\\u001B[A")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_CHANNEL_DOWN) {
                                onWriteRawToConsoleStdin("\\u001B[B")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {"""

if target in content:
    content = content.replace(target, replacement)
    with open(file_path, "w") as f:
        f.write(content)
    print("Replaced successfully")
else:
    print("Not replaced")
