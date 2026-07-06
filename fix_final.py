import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Remove isPasswordFocused variable
old_vars = """    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }"""
new_vars = """    var passwordVisible by remember { mutableStateOf(false) }"""
content = content.replace(old_vars, new_vars)

# 2. Remove isPasswordFocused from onFocusChanged
old_focus = """                        modifier = Modifier.weight(1f).onFocusChanged {
                            isPasswordFocused = it.isFocused
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    val lastIndex = listState.layoutInfo.totalItemsCount - 1
                                    if (lastIndex >= 0) {
                                        listState.animateScrollToItem(lastIndex)
                                    }
                                }
                            }
                        },"""
new_focus = """                        modifier = Modifier.weight(1f).onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    val lastIndex = listState.layoutInfo.totalItemsCount - 1
                                    if (lastIndex >= 0) {
                                        listState.animateScrollToItem(lastIndex)
                                    }
                                }
                            }
                        },"""
content = content.replace(old_focus, new_focus)

# 3. Replace the spacer condition at the bottom
old_spacer = """            if (isTv && isPasswordFocused) {
                item {
                    Spacer(modifier = Modifier.height(250.dp))
                }
            }"""
new_spacer = """            item {
                Spacer(modifier = Modifier.height(20.dp))
            }"""
content = content.replace(old_spacer, new_spacer)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
