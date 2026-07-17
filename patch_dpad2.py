import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

target = """                    view.onSendKeyEvent = { keyEvent, isIme ->
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
                                val isKeyboard = isIme || keyEvent.deviceId < 0 || keyEvent.device?.isVirtual == true || keyEvent.device?.keyboardType == android.view.InputDevice.KEYBOARD_TYPE_ALPHABETIC
                                if (isKeyboard || !isTv) {
                                    onWriteRawToConsoleStdin("\\u001B[D")
                                } else {
                                    try { clearButtonFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
                                val isKeyboard = isIme || keyEvent.deviceId < 0 || keyEvent.device?.isVirtual == true || keyEvent.device?.keyboardType == android.view.InputDevice.KEYBOARD_TYPE_ALPHABETIC
                                if (isKeyboard || !isTv) {
                                    onWriteRawToConsoleStdin("\\u001B[C")
                                } else {
                                    try { termKeysFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {"""

replacement = """                    view.onSendKeyEvent = { keyEvent, isIme ->
                        if (keyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
                            if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) {
                                if (listState.canScrollBackward) {
                                    scope.launch { listState.animateScrollBy(-150f) }
                                } else {
                                    try { clearButtonFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (listState.canScrollForward) {
                                    scope.launch { listState.animateScrollBy(150f) }
                                } else {
                                    try { termKeysFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
                                onWriteRawToConsoleStdin("\\u001B[D")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
                                onWriteRawToConsoleStdin("\\u001B[C")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {"""

if target in content:
    content = content.replace(target, replacement)
    with open(file_path, "w") as f:
        f.write(content)
    print("Patched MainActivity for scrolling and focus")
else:
    print("Target not found for dpad2")
