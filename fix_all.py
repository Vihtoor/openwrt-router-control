import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Add interaction imports
if "import androidx.compose.foundation.interaction.MutableInteractionSource" not in content:
    content = content.replace("import androidx.compose.foundation.layout.*", "import androidx.compose.foundation.layout.*\nimport androidx.compose.foundation.interaction.MutableInteractionSource\nimport androidx.compose.foundation.interaction.collectIsFocusedAsState\nimport androidx.compose.foundation.LocalIndication")

# 1. Spacer 250dp -> 20dp
old_spacer = """            if (isTv) {
                item {
                    Spacer(modifier = Modifier.height(250.dp))
                }
            }"""
new_spacer = """            item {
                Spacer(modifier = Modifier.height(20.dp))
            }"""
content = content.replace(old_spacer, new_spacer)

# 2. Add keys to items(configs)
content = content.replace("items(configs) { config ->", "items(items = configs, key = { it.id }) { config ->")

# 3. Fix Close Button
old_close = """                var isCloseFocused by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .onFocusChanged { isCloseFocused = it.isFocused }
                        .background(if (isCloseFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close, 
                        contentDescription = translateText("Закрыть", context),
                        tint = if (isCloseFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                    )
                }"""
new_close = """                val closeInteractionSource = remember { MutableInteractionSource() }
                val isCloseFocused by closeInteractionSource.collectIsFocusedAsState()
                IconButton(
                    onClick = onDismiss,
                    interactionSource = closeInteractionSource,
                    modifier = Modifier
                        .background(if (isCloseFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Close, 
                        contentDescription = translateText("Закрыть", context),
                        tint = if (isCloseFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                    )
                }"""
content = content.replace(old_close, new_close)

# 4. Fix Router profile Row
old_row = """                            var isRowFocused by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .onFocusChanged { isRowFocused = it.isFocused }
                                    .background(if (isRowFocused) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { onSelect(config.id) }
                                    .padding(8.dp),"""
new_row = """                            val rowInteractionSource = remember { MutableInteractionSource() }
                            val isRowFocused by rowInteractionSource.collectIsFocusedAsState()
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(if (isRowFocused) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable(
                                        interactionSource = rowInteractionSource,
                                        indication = LocalIndication.current
                                    ) { onSelect(config.id) }
                                    .padding(8.dp),"""
content = content.replace(old_row, new_row)

# 5. Fix Edit/Delete buttons
old_edit_delete = """                            var isEditFocused by remember { mutableStateOf(false) }
                            var isDeleteFocused by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .onFocusChanged { isEditFocused = it.isFocused }
                                    .background(if (isEditFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                    .clickable { onEdit(config) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Edit, 
                                    contentDescription = translateText("Редактировать", context),
                                    tint = if (isEditFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .onFocusChanged { isDeleteFocused = it.isFocused }
                                    .background(if (isDeleteFocused) MaterialTheme.colorScheme.errorContainer else Color.Transparent)
                                    .clickable { onDelete(config.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Delete, 
                                    contentDescription = translateText("Удалить", context),
                                    tint = if (isDeleteFocused) MaterialTheme.colorScheme.onErrorContainer else LocalContentColor.current
                                )
                            }"""
new_edit_delete = """                            val editInteractionSource = remember { MutableInteractionSource() }
                            val isEditFocused by editInteractionSource.collectIsFocusedAsState()
                            val deleteInteractionSource = remember { MutableInteractionSource() }
                            val isDeleteFocused by deleteInteractionSource.collectIsFocusedAsState()
                            IconButton(
                                onClick = { onEdit(config) },
                                interactionSource = editInteractionSource,
                                modifier = Modifier
                                    .background(if (isEditFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Edit, 
                                    contentDescription = translateText("Редактировать", context),
                                    tint = if (isEditFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                                )
                            }
                            IconButton(
                                onClick = { onDelete(config.id) },
                                interactionSource = deleteInteractionSource,
                                modifier = Modifier
                                    .background(if (isDeleteFocused) MaterialTheme.colorScheme.errorContainer else Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Delete, 
                                    contentDescription = translateText("Удалить", context),
                                    tint = if (isDeleteFocused) MaterialTheme.colorScheme.onErrorContainer else LocalContentColor.current
                                )
                            }"""
content = content.replace(old_edit_delete, new_edit_delete)

# 6. Fix Password visibility button
old_pwd = """                    var isIconFocused by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .onFocusChanged { isIconFocused = it.isFocused }
                            .background(if (isIconFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            .clickable { passwordVisible = !passwordVisible },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = image, 
                            contentDescription = null,
                            tint = if (isIconFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                        )
                    }"""
new_pwd = """                    val iconInteractionSource = remember { MutableInteractionSource() }
                    val isIconFocused by iconInteractionSource.collectIsFocusedAsState()
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        interactionSource = iconInteractionSource,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(if (isIconFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            imageVector = image, 
                            contentDescription = null,
                            tint = if (isIconFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                        )
                    }"""
content = content.replace(old_pwd, new_pwd)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
