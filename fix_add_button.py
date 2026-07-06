import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_add = """            Button(
                onClick = onAddNew,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = translateText("Добавить новый роутер", context))
            }"""
new_add = """            val addInteractionSource = remember { MutableInteractionSource() }
            val isAddFocused by addInteractionSource.collectIsFocusedAsState()
            Button(
                onClick = onAddNew,
                interactionSource = addInteractionSource,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAddFocused) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isAddFocused) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = translateText("Добавить новый роутер", context))
            }"""
content = content.replace(old_add, new_add)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
