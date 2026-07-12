with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("""                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(cmd))
                                        onNavigateToConsole()
                                    }""", """                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(cmd))
                                        onNavigateToConsole(cmd)
                                    }""")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
