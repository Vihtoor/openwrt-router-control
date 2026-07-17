import re

file_path = "app/src/main/java/com/example/ui/TerminalInputView.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """    var onSendKeyEvent: (KeyEvent) -> Boolean = { false }
) : View(context) {"""

replacement = """    var onSendKeyEvent: (KeyEvent) -> Boolean = { false }
) : View(context) {

    var shouldRequestFocusOnWindowFocus = true"""

content = content.replace(target, replacement)

target2 = """    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {"""

replacement2 = """    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && shouldRequestFocusOnWindowFocus) {"""

content = content.replace(target2, replacement2)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching TerminalInputView")
