import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_close = """                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = translateText("Закрыть", context))
                }"""

new_close = """                var isCloseFocused by remember { mutableStateOf(false) }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .focusable()
                        .onFocusChanged { isCloseFocused = it.isFocused }
                        .background(if (isCloseFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Close, 
                        contentDescription = translateText("Закрыть", context),
                        tint = if (isCloseFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                    )
                }"""

content = content.replace(old_close, new_close)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
