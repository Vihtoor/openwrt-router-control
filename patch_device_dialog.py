with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("""                                        onClick = {
                                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                            onNavigateToConsole()
                                        }""", """                                        onClick = {
                                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                            onNavigateToConsole("opkg update && opkg install nlbwmon")
                                        }""")
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
