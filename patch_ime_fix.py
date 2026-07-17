import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

target = """                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
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

replacement = """                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
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
                            }"""

if target in content:
    content = content.replace(target, replacement)
    with open(file_path, "w") as f:
        f.write(content)
    print("Patched MainActivity for IME arrows")
else:
    print("Target not found!")
