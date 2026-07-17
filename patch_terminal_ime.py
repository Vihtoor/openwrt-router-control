import re

file_path = "app/src/main/java/com/example/ui/TerminalInputView.kt"
with open(file_path, "r") as f:
    content = f.read()

content = content.replace("var onSendKeyEvent: (KeyEvent) -> Boolean = { false }", "var onSendKeyEvent: (KeyEvent, Boolean) -> Boolean = { _, _ -> false }")
content = content.replace("val handled = onSendKeyEvent(event)", "val handled = onSendKeyEvent(event, true)")
content = content.replace("""    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val handled = onSendKeyEvent(event, true)
        return if (handled) true else super.dispatchKeyEvent(event)
    }""", """    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val handled = onSendKeyEvent(event, false)
        return if (handled) true else super.dispatchKeyEvent(event)
    }""")

with open(file_path, "w") as f:
    f.write(content)
print("Patched TerminalInputView")
