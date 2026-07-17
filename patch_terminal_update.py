import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                update = { view ->
                    view.shouldRequestFocusOnWindowFocus = !showConsoleInteractiveTip
                    view.isFocusable = !showConsoleInteractiveTip
                    view.isFocusableInTouchMode = !showConsoleInteractiveTip
                    if (showConsoleInteractiveTip) view.clearFocus()
                    view.onProcessIncomingText = { insertedText ->"""

replacement = """                update = { view ->
                    view.shouldRequestFocusOnWindowFocus = !showConsoleInteractiveTip
                    view.isFocusable = !showConsoleInteractiveTip
                    view.isFocusableInTouchMode = !showConsoleInteractiveTip
                    if (showConsoleInteractiveTip) {
                        view.clearFocus()
                        val imm = view.context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    view.onProcessIncomingText = { insertedText ->"""

content = content.replace(target, replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching terminal update")
