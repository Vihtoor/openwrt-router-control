import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

target = """                    view.onSendKeyEvent = { keyEvent ->
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
                                onWriteRawToConsoleStdin("\\u001B[D")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
                                onWriteRawToConsoleStdin("\\u001B[C")
                                true
                            }"""

replacement = """                    view.onSendKeyEvent = { keyEvent, isIme ->
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
                                if (isIme || !isTv) {
                                    onWriteRawToConsoleStdin("\\u001B[D")
                                } else {
                                    try { clearButtonFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
                                if (isIme || !isTv) {
                                    onWriteRawToConsoleStdin("\\u001B[C")
                                } else {
                                    try { termKeysFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            }"""

if target in content:
    content = content.replace(target, replacement)
    with open(file_path, "w") as f:
        f.write(content)
    print("Patched MainActivity")
else:
    print("Target not found")
