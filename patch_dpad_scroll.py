import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

target = """                            if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) {
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
                            }"""

replacement = """                            if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) {
                                if (listState.canScrollForward) {
                                    scope.launch { listState.animateScrollBy(150f) }
                                } else {
                                    try { clearButtonFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (listState.canScrollBackward) {
                                    scope.launch { listState.animateScrollBy(-150f) }
                                } else {
                                    try { termKeysFocusRequester.requestFocus() } catch (e: Exception) {}
                                }
                                true
                            }"""

if target in content:
    content = content.replace(target, replacement)
    print("Patched successfully")
else:
    print("Target not found")

with open(file_path, "w") as f:
    f.write(content)
