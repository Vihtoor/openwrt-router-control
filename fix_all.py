with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. DeviceListDialog right pane
target1 = """                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Установите утилиту nlbwmon для сбора статистики:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.Button(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                    },
                                    modifier = Modifier.padding(top = 8.dp).onFocusChanged { btnFocused = it.isFocused },
                                    border = if (btnFocused) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                                ) {
                                    Text("Скопировать команду")
                                }"""

replacement1 = """                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Установите утилиту nlbwmon для сбора статистики:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "opkg update && opkg install nlbwmon",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    var btnFocused by remember { mutableStateOf(false) }
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                        },
                                        modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }"""

content = content.replace(target1, replacement1)

# 2. DeviceSpeedDialog popup
target2 = """                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Установите утилиту nlbwmon для сбора статистики:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    var btnFocused by remember { mutableStateOf(false) }
                    androidx.compose.material3.Button(
                        onClick = {
                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                        },
                        modifier = Modifier.padding(top = 8.dp).onFocusChanged { btnFocused = it.isFocused },
                        border = if (btnFocused) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                    ) {
                        Text("Скопировать команду")
                    }"""

replacement2 = """                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Установите утилиту nlbwmon для сбора статистики:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "opkg update && opkg install nlbwmon",
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        var btnFocused by remember { mutableStateOf(false) }
                        androidx.compose.material3.IconButton(
                            onClick = {
                                clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                            },
                            modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }"""

content = content.replace(target2, replacement2)

# 3. Help Dialog
target3 = """                            } else if (paragraph.startsWith("opkg update")) {
                                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                        }
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }"""

replacement3 = """                            } else if (paragraph.startsWith("opkg update")) {
                                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    var btnFocused by remember { mutableStateOf(false) }
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                        },
                                        modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }"""

content = content.replace(target3, replacement3)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
