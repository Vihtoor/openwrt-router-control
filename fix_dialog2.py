import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_header_bg = "val headerBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)"
new_header_bg = "val headerBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)"
content = content.replace(old_header_bg, new_header_bg)

old_divider = """                                        androidx.compose.material3.Divider(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                            thickness = 1.dp
                                        )"""
new_divider = """                                        androidx.compose.material3.Divider(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                                            thickness = 1.dp
                                        )"""
content = content.replace(old_divider, new_divider)

old_row = """                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(device.hostname, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                            Text(device.connectionType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps"""
new_row = """                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(device.hostname, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                            Text(device.connectionType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        val speed = if (isDownload) device.downloadSpeedMbps else device.uploadSpeedMbps"""
content = content.replace(old_row, new_row)

old_bottom_panel = """                    // Bottom Panel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        var isFocused by remember { mutableStateOf(false) }
                        androidx.compose.material3.TextButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
                        ) {
                            Text("Закрыть")
                        }
                    }"""

new_bottom_panel = """                    // Bottom Panel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        var isFocused by remember { mutableStateOf(false) }
                        androidx.compose.material3.TextButton(
                            onClick = onDismiss,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
                        ) {
                            Text("Закрыть")
                        }
                    }"""
content = content.replace(old_bottom_panel, new_bottom_panel)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

