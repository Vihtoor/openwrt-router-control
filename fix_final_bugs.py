import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Add imports if missing
if "import androidx.compose.foundation.layout.isImeVisible" not in content:
    content = content.replace("import androidx.compose.foundation.layout.WindowInsets", "import androidx.compose.foundation.layout.WindowInsets\nimport androidx.compose.foundation.layout.isImeVisible\nimport androidx.compose.foundation.layout.ime")
    if "import androidx.compose.foundation.layout.WindowInsets" not in content:
        content = content.replace("import androidx.compose.foundation.layout.*", "import androidx.compose.foundation.layout.*\nimport androidx.compose.foundation.layout.WindowInsets\nimport androidx.compose.foundation.layout.isImeVisible\nimport androidx.compose.foundation.layout.ime")

# Add isPasswordFocused state
if "var isPasswordFocused by remember { mutableStateOf(false) }" not in content:
    content = content.replace("var passwordVisible by remember { mutableStateOf(false) }", 
                              "var passwordVisible by remember { mutableStateOf(false) }\n    var isPasswordFocused by remember { mutableStateOf(false) }")

# Watch for IME visibility
ime_watcher = """    val listState = rememberLazyListState()
    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (isImeVisible && isPasswordFocused) {
            kotlinx.coroutines.delay(200)
            listState.animateScrollBy(10000f)
        }
    }"""
content = content.replace("    val listState = rememberLazyListState()", ime_watcher)

# Update the password field focus modifier
old_password_focus = """                        modifier = Modifier.weight(1f).onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    listState.animateScrollBy(10000f)
                                }
                            }
                        },"""
new_password_focus = """                        modifier = Modifier.weight(1f).onFocusChanged {
                            isPasswordFocused = it.isFocused
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    listState.animateScrollBy(10000f)
                                }
                            }
                        },"""
content = content.replace(old_password_focus, new_password_focus)

# Update Button Colors
# Add Button
old_add = """                    containerColor = if (isAddFocused) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isAddFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondary"""
new_add = """                    containerColor = if (isAddFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isAddFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSecondary"""
content = content.replace(old_add, new_add)

# Cancel Button
old_cancel = """                                containerColor = if (isCancelFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                contentColor = if (isCancelFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary"""
new_cancel = """                                containerColor = if (isCancelFocused) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                                contentColor = if (isCancelFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary"""
content = content.replace(old_cancel, new_cancel)

# Test Button
old_test = """                            containerColor = if (isTestFocused) MaterialTheme.colorScheme.inversePrimary else if (isConnectionVerified) androidx.compose.ui.graphics.Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            contentColor = if (isConnectionVerified) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onPrimary"""
new_test = """                            containerColor = if (isTestFocused) MaterialTheme.colorScheme.tertiary else if (isConnectionVerified) androidx.compose.ui.graphics.Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            contentColor = if (isTestFocused) MaterialTheme.colorScheme.onTertiary else if (isConnectionVerified) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onPrimary"""
content = content.replace(old_test, new_test)

# Save Button
old_save = """                        containerColor = if (isSaveFocused) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(translateText("Сохранить профиль", context), color = if (isSaveFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary)"""
new_save = """                        containerColor = if (isSaveFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(translateText("Сохранить профиль", context), color = if (isSaveFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary)"""
content = content.replace(old_save, new_save)


with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
