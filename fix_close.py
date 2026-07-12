import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_bottom_panel = """                    // Bottom Panel
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
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
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
