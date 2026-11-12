import sys

def modify_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    target = """                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { loginMethod = 0; sshKeyId = null; onFieldChanged() },
                        modifier = Modifier.weight(1f).height(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (loginMethod == 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (loginMethod == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (loginMethod == 0) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text(translateText("Вход по паролю", context), fontSize = 13.sp)
                    }
                    Button(
                        onClick = { loginMethod = 1; password = ""; onFieldChanged() },
                        modifier = Modifier.weight(1f).height(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (loginMethod == 1) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (loginMethod == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (loginMethod == 1) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text(translateText("Вход по SSH ключу", context), fontSize = 13.sp)
                    }
                }"""
                
    replacement = """                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val passInteractionSource = remember { MutableInteractionSource() }
                    val isPassFocused by passInteractionSource.collectIsFocusedAsState()
                    Button(
                        onClick = { loginMethod = 0; sshKeyId = null; onFieldChanged() },
                        interactionSource = passInteractionSource,
                        modifier = Modifier.weight(1f).height(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTv && isPassFocused) MaterialTheme.colorScheme.tertiary else if (loginMethod == 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isTv && isPassFocused) MaterialTheme.colorScheme.onTertiary else if (loginMethod == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (!isTv && loginMethod == 0) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else if (isTv && loginMethod == 0 && !isPassFocused) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text(translateText("Вход по паролю", context), fontSize = 13.sp)
                    }
                    val sshInteractionSource = remember { MutableInteractionSource() }
                    val isSshFocused by sshInteractionSource.collectIsFocusedAsState()
                    Button(
                        onClick = { loginMethod = 1; password = ""; onFieldChanged() },
                        interactionSource = sshInteractionSource,
                        modifier = Modifier.weight(1f).height(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTv && isSshFocused) MaterialTheme.colorScheme.tertiary else if (loginMethod == 1) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isTv && isSshFocused) MaterialTheme.colorScheme.onTertiary else if (loginMethod == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (!isTv && loginMethod == 1) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else if (isTv && loginMethod == 1 && !isSshFocused) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text(translateText("Вход по SSH ключу", context), fontSize = 13.sp)
                    }
                }"""

    if target in content:
        content = content.replace(target, replacement)
        print("SettingsPanel Target replaced.")
    else:
        print("SettingsPanel Target NOT found.")
        
    with open(filepath, 'w') as f:
        f.write(content)

modify_file('app/src/main/java/com/example/ui/SettingsPanel.kt')
