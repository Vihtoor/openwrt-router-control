import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Fix paste button
paste_button_old = """                        clipboardManager.getText()?.text?.let { pasteText ->
                            pasteText.forEach { char ->
                                val toSend = if (char == '\n') "\r" else char.toString()
                                onWriteRawToConsoleStdin(toSend)
                                if (char == '\n' || char == '\r') {
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
                        }"""
paste_button_new = """                        clipboardManager.getText()?.text?.let { pasteText ->
                            val cleanPaste = pasteText.replace("\\n", "").replace("\\r", "")
                            cleanPaste.forEach { char ->
                                onWriteRawToConsoleStdin(char.toString())
                                currentTypedLine += char
                            }
                        }"""
if paste_button_old in content:
    content = content.replace(paste_button_old, paste_button_new)
    print("Fixed paste button")
else:
    print("Paste button not found")

# Fix view.onHandlePaste
handle_paste_old = """                    view.onHandlePaste = {
                        val pastedText = clipboardManagerLocal.getText()?.text
                        if (pastedText != null) {
                            view.onProcessIncomingText(pastedText.toString())
                        }
                    }"""
handle_paste_new = """                    view.onHandlePaste = {
                        val pastedText = clipboardManagerLocal.getText()?.text
                        if (pastedText != null) {
                            val cleanPaste = pastedText.toString().replace("\\n", "").replace("\\r", "")
                            view.onProcessIncomingText(cleanPaste)
                        }
                    }"""
if handle_paste_old in content:
    content = content.replace(handle_paste_old, handle_paste_new)
    print("Fixed handle paste")
else:
    print("Handle paste not found")

# Fix favorites
fav_clickable_old = """                                                .clickable {
                                                    onWriteRawToConsoleStdin(favCmd + "\\r")
                                                    showFavoritesDialog = false
                                                }"""
fav_clickable_new = """                                                .clickable {
                                                    val cleanCmd = favCmd.replace("\\n", "").replace("\\r", "")
                                                    cleanCmd.forEach { char ->
                                                        onWriteRawToConsoleStdin(char.toString())
                                                        currentTypedLine += char
                                                    }
                                                    showFavoritesDialog = false
                                                }"""
if fav_clickable_old in content:
    content = content.replace(fav_clickable_old, fav_clickable_new)
    print("Fixed fav clickable")
else:
    print("Fav clickable not found")

fav_key_old = """                                                        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                            onWriteRawToConsoleStdin(favCmd + "\\r")
                                                            showFavoritesDialog = false
                                                            true
                                                        }"""
fav_key_new = """                                                        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                            val cleanCmd = favCmd.replace("\\n", "").replace("\\r", "")
                                                            cleanCmd.forEach { char ->
                                                                onWriteRawToConsoleStdin(char.toString())
                                                                currentTypedLine += char
                                                            }
                                                            showFavoritesDialog = false
                                                            true
                                                        }"""
if fav_key_old in content:
    content = content.replace(fav_key_old, fav_key_new)
    print("Fixed fav key")
else:
    print("Fav key not found")

# Fix history
hist_clickable_old = """                                                .clickable {
                                                    onWriteRawToConsoleStdin(itemCmd + "\\r")
                                                    showHistoryDialog = false
                                                }"""
hist_clickable_new = """                                                .clickable {
                                                    val cleanCmd = itemCmd.replace("\\n", "").replace("\\r", "")
                                                    cleanCmd.forEach { char ->
                                                        onWriteRawToConsoleStdin(char.toString())
                                                        currentTypedLine += char
                                                    }
                                                    showHistoryDialog = false
                                                }"""
if hist_clickable_old in content:
    content = content.replace(hist_clickable_old, hist_clickable_new)
    print("Fixed hist clickable")
else:
    print("Hist clickable not found")

hist_key_old = """                                                        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                            onWriteRawToConsoleStdin(itemCmd + "\\r")
                                                            showHistoryDialog = false
                                                            true
                                                        }"""
hist_key_new = """                                                        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                            val cleanCmd = itemCmd.replace("\\n", "").replace("\\r", "")
                                                            cleanCmd.forEach { char ->
                                                                onWriteRawToConsoleStdin(char.toString())
                                                                currentTypedLine += char
                                                            }
                                                            showHistoryDialog = false
                                                            true
                                                        }"""
if hist_key_old in content:
    content = content.replace(hist_key_old, hist_key_new)
    print("Fixed hist key")
else:
    print("Hist key not found")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

