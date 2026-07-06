import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Add isPasswordFocused state
old_vars = """    var password by remember { mutableStateOf(config.sshKeyOrPassword) }
    var passwordVisible by remember { mutableStateOf(false) }"""
new_vars = """    var password by remember { mutableStateOf(config.sshKeyOrPassword) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }"""
content = content.replace(old_vars, new_vars)

# 2. Update onFocusChanged for password
old_focus = """                        modifier = Modifier.weight(1f).onFocusChanged {
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
content = content.replace(old_focus, new_focus)

# 3. Fix the bottom spacer
old_bottom = """                Button(
                    onClick = {
                        val p = port.toIntOrNull() ?: 22
                        // "wgInterface" defaults to wg0 here, ledBehavior defaults to always_on
                        onSave(profileName, ip, p, user, password, config.ledBehavior, config.wgInterface)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(translateText("Сохранить профиль", context))
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}"""
new_bottom = """                Button(
                    onClick = {
                        val p = port.toIntOrNull() ?: 22
                        // "wgInterface" defaults to wg0 here, ledBehavior defaults to always_on
                        onSave(profileName, ip, p, user, password, config.ledBehavior, config.wgInterface)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(translateText("Сохранить профиль", context))
                }
            }
            if (isTv && isPasswordFocused) {
                item {
                    Spacer(modifier = Modifier.height(250.dp))
                }
            }
        }
    }
}"""
content = content.replace(old_bottom, new_bottom)

# 4. Fix LazyColumn padding to be 20dp at bottom
old_lc_pad = """            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()"""
new_lc_pad = """            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 20.dp)
                .fillMaxWidth()"""
content = content.replace(old_lc_pad, new_lc_pad)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
