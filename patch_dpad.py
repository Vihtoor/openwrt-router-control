import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

# Add terminalWindowFocusRequester
req_target = "val clearButtonFocusRequester = remember { FocusRequester() }"
if "val terminalWindowFocusRequester = remember { FocusRequester() }" not in content:
    content = content.replace(req_target, "val terminalWindowFocusRequester = remember { FocusRequester() }\n    " + req_target)

# Add modifier to AndroidView
android_view_target = """            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->"""
android_view_replacement = """            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.focusRequester(terminalWindowFocusRequester).focusProperties {
                    up = clearButtonFocusRequester
                    down = termKeysFocusRequester
                },
                factory = { ctx ->"""
content = content.replace(android_view_target, android_view_replacement)

# Update focusProperties for clearButton
clear_btn_target = """.focusProperties {
                            left = pasteBtnFocusRequester
                        }"""
clear_btn_replacement = """.focusProperties {
                            left = pasteBtnFocusRequester
                            down = terminalWindowFocusRequester
                        }"""
content = content.replace(clear_btn_target, clear_btn_replacement)

# Update focusProperties for pasteButton
paste_btn_target = """.focusProperties {
                            left = copyBtnFocusRequester
                            right = clearButtonFocusRequester
                        }"""
paste_btn_replacement = """.focusProperties {
                            left = copyBtnFocusRequester
                            right = clearButtonFocusRequester
                            down = terminalWindowFocusRequester
                        }"""
content = content.replace(paste_btn_target, paste_btn_replacement)

# Update focusProperties for copyButton
copy_btn_target = """.focusProperties {
                            left = increaseBtnFocusRequester
                            right = pasteBtnFocusRequester
                        }"""
copy_btn_replacement = """.focusProperties {
                            left = increaseBtnFocusRequester
                            right = pasteBtnFocusRequester
                            down = terminalWindowFocusRequester
                        }"""
content = content.replace(copy_btn_target, copy_btn_replacement)

# Update focusProperties for increaseButton
increase_btn_target = """.focusProperties {
                            left = decreaseBtnFocusRequester
                            right = copyBtnFocusRequester
                        }"""
increase_btn_replacement = """.focusProperties {
                            left = decreaseBtnFocusRequester
                            right = copyBtnFocusRequester
                            down = terminalWindowFocusRequester
                        }"""
content = content.replace(increase_btn_target, increase_btn_replacement)

# Update focusProperties for decreaseButton
decrease_btn_target = """.focusProperties {
                            right = increaseBtnFocusRequester
                        }"""
decrease_btn_replacement = """.focusProperties {
                            right = increaseBtnFocusRequester
                            down = terminalWindowFocusRequester
                        }"""
content = content.replace(decrease_btn_target, decrease_btn_replacement)

# Update termKeys box
termkeys_box_target = """                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isKeyFocused) MaterialTheme.colorScheme.primary else Color(0xFF2D2D36))
                            .then(itemModifier)
                            .onFocusChanged { isKeyFocused = it.isFocused }
                            .focusable()"""
termkeys_box_replacement = """                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isKeyFocused) MaterialTheme.colorScheme.primary else Color(0xFF2D2D36))
                            .then(itemModifier)
                            .onFocusChanged { isKeyFocused = it.isFocused }
                            .focusProperties { up = terminalWindowFocusRequester }
                            .focusable()"""
content = content.replace(termkeys_box_target, termkeys_box_replacement)

with open(file_path, "w") as f:
    f.write(content)
