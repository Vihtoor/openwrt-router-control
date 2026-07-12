with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    var btnFocused by remember { mutableStateOf(false) }
                    androidx.compose.material3.Button(
                        onClick = {
                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                        },
                        modifier = Modifier.padding(top = 8.dp).onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent)
                    ) {"""

replacement = """                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    var btnFocused by remember { mutableStateOf(false) }
                    androidx.compose.material3.Button(
                        onClick = {
                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                        },
                        modifier = Modifier.padding(top = 8.dp).onFocusChanged { btnFocused = it.isFocused },
                        border = if (btnFocused) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                    ) {"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
