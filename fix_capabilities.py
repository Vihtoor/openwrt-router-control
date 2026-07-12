with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                if (action.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(action, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }"""

replacement = """                if (action.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Установите утилиты для сбора статистики:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    for (act in actions) {
                        val cmd = if (act.contains("ip-bridge")) "opkg update && opkg install ip-bridge" else if (act.contains("swconfig")) "opkg update && opkg install swconfig" else ""
                        if (cmd.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cmd,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.IconButton(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(cmd))
                                    },
                                    modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        } else {
                            Text(act, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
