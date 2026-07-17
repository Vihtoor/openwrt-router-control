import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = r"view\.onSendKeyEvent = \{ keyEvent ->[\s\S]*?\}\s+else\s*\{\s*false\s*\}\s*\}"

replacement = """view.onSendKeyEvent = { keyEvent ->
                        if (keyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
                            val isUpDown = keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP || keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN
                            if (isUpDown) {
                                if (consoleHistory.isNotEmpty()) {
                                    scope.launch {
                                        val amount = if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) -150f else 150f
                                        listState.animateScrollBy(amount)
                                    }
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
                                try { clearButtonFocusRequester.requestFocus() } catch (e: Exception) {}
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
                                try { termKeysFocusRequester.requestFocus() } catch (e: Exception) {}
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
                                val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                                imm.showSoftInput(view, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                view.onProcessIncomingText("\\r")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                                view.onDeleteSurroundingText(1, 0)
                                true
                            } else {
                                false
                            }
                        } else {
                            false
                        }
                    }"""

new_content = re.sub(target, replacement, content, count=1)
if content != new_content:
    with open(file_path, "w") as f:
        f.write(new_content)
    print("Replaced successfully")
else:
    print("Not replaced")
