import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# I will replace the `else { ... }` block inside the body.split loop.
# Notice the indentation is around 8 spaces for the `else {` line.

search = """                            } else {
                                val boldRegex = Regex("\\\\*\\\\*(.*?)\\\\*\\\\*")"""
                                
replace = """                            } else if (paragraph.startsWith("opkg update")) {
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
                                }
                            } else {
                                val boldRegex = Regex("\\\\*\\\\*(.*?)\\\\*\\\\*")"""

content = content.replace(search, replace)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

print("done")
