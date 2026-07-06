import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Fix Close Button
old_close = """                var isCloseFocused by remember { mutableStateOf(false) }
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
new_close = """                var isCloseFocused by remember { mutableStateOf(false) }
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
content = content.replace(old_close, new_close)

# Fix Edit Button
old_edit = """                            var isEditFocused by remember { mutableStateOf(false) }
                            var isDeleteFocused by remember { mutableStateOf(false) }
                            IconButton(
                                onClick = { onEdit(config) },
                                modifier = Modifier
                                    .focusable()
                                    .onFocusChanged { isEditFocused = it.isFocused }
                                    .background(if (isEditFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Edit, 
                                    contentDescription = translateText("Редактировать", context),
                                    tint = if (isEditFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                                )
                            }"""
new_edit = """                            var isEditFocused by remember { mutableStateOf(false) }
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
                            }"""
content = content.replace(old_edit, new_edit)

# Fix Delete Button
old_delete = """                            IconButton(
                                onClick = { onDelete(config.id) },
                                modifier = Modifier
                                    .focusable()
                                    .onFocusChanged { isDeleteFocused = it.isFocused }
                                    .background(if (isDeleteFocused) MaterialTheme.colorScheme.errorContainer else Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Delete, 
                                    contentDescription = translateText("Удалить", context),
                                    tint = if (isDeleteFocused) MaterialTheme.colorScheme.onErrorContainer else LocalContentColor.current
                                )
                            }"""
new_delete = """                            Box(
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
content = content.replace(old_delete, new_delete)

# Fix Password Visibility Button
old_pwd = """                    var isIconFocused by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .focusable()
                            .onFocusChanged { isIconFocused = it.isFocused }
                            .background(if (isIconFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            imageVector = image, 
                            contentDescription = null,
                            tint = if (isIconFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                        )
                    }"""
new_pwd = """                    var isIconFocused by remember { mutableStateOf(false) }
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
content = content.replace(old_pwd, new_pwd)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
