import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    com.example.ui.TerminalInputView(ctx).apply {
                        terminalView = this
                    }
                },
                update = { view ->
                    view.onProcessIncomingText = { insertedText ->"""

replacement = """            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    com.example.ui.TerminalInputView(ctx).apply {
                        terminalView = this
                    }
                },
                update = { view ->
                    view.shouldRequestFocusOnWindowFocus = !showConsoleInteractiveTip
                    view.onProcessIncomingText = { insertedText ->"""

content = content.replace(target, replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching terminal view usage")
