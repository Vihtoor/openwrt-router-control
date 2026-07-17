import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

paste_old = """                    onClick = {
                        clipboardManager.getText()?.text?.let { pasteText ->
                            pasteText.forEach { char ->
                                val toSend = if (char == '\\n') "\\r" else char.toString()
                                onWriteRawToConsoleStdin(toSend)
                                if (char == '\\n' || char == '\\r') {
                                    val cmd = currentTypedLine.trim()
                                    if (cmd.isNotEmpty()) {
                                        lastEnteredCommand = cmd
                                        onAddHistoryItem(cmd)
                                    }
                                    currentTypedLine = ""
                                } else {
                                    currentTypedLine += char
                                }
                            }
                        }
                    },"""

paste_new = """                    onClick = {
                        clipboardManager.getText()?.text?.let { pasteText ->
                            val cleanPaste = pasteText.replace("\\n", "").replace("\\r", "")
                            cleanPaste.forEach { char ->
                                onWriteRawToConsoleStdin(char.toString())
                                currentTypedLine += char
                            }
                        }
                    },"""

if paste_old in content:
    content = content.replace(paste_old, paste_new)
    print("Replaced paste button")
else:
    print("Not found")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
