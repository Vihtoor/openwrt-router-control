with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                        androidx.compose.material3.IconButton(
                            onClick = {
                                clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                            },
                            modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                        ) {"""

replacement = """                        androidx.compose.material3.IconButton(
                            onClick = {
                                clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                            },
                            modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                        ) {"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
