import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Replace OutlinedButton (Cancel)
old_cancel = """                    if (!isFirst) {
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(translateText("Отмена", context))
                        }
                    }"""
new_cancel = """                    if (!isFirst) {
                        val cancelInteractionSource = remember { MutableInteractionSource() }
                        val isCancelFocused by cancelInteractionSource.collectIsFocusedAsState()
                        OutlinedButton(
                            onClick = onCancel,
                            interactionSource = cancelInteractionSource,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCancelFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                contentColor = if (isCancelFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(translateText("Отмена", context))
                        }
                    }"""
content = content.replace(old_cancel, new_cancel)

# Replace Button (Test connection)
old_test = """                    Button(
                        onClick = {
                            val p = port.toIntOrNull() ?: 22
                            onTest(ip, p, user, password)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isConnecting,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isConnectionVerified) androidx.compose.ui.graphics.Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            contentColor = if (isConnectionVerified) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {"""
new_test = """                    val testInteractionSource = remember { MutableInteractionSource() }
                    val isTestFocused by testInteractionSource.collectIsFocusedAsState()
                    Button(
                        onClick = {
                            val p = port.toIntOrNull() ?: 22
                            onTest(ip, p, user, password)
                        },
                        interactionSource = testInteractionSource,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isConnecting,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTestFocused) MaterialTheme.colorScheme.inversePrimary else if (isConnectionVerified) androidx.compose.ui.graphics.Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            contentColor = if (isConnectionVerified) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {"""
content = content.replace(old_test, new_test)

# Replace Button (Save profile)
old_save = """            item {
                Button(
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
            }"""
new_save = """            item {
                val saveInteractionSource = remember { MutableInteractionSource() }
                val isSaveFocused by saveInteractionSource.collectIsFocusedAsState()
                Button(
                    onClick = {
                        val p = port.toIntOrNull() ?: 22
                        onSave(profileName, ip, p, user, password, config.ledBehavior, config.wgInterface)
                    },
                    interactionSource = saveInteractionSource,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveFocused) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(translateText("Сохранить профиль", context), color = if (isSaveFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary)
                }
            }"""
content = content.replace(old_save, new_save)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
