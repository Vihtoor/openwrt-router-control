with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("""            onNavigateToConsole = {
                showDevices = false
                viewModel.switchTab(TabType.CONSOLE)
            }""", """            onNavigateToConsole = { cmd ->
                showDevices = false
                if (cmd != null) {
                    viewModel.setCommandInput(cmd)
                }
                viewModel.switchTab(TabType.CONSOLE)
            }""")

content = content.replace("""                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                        }""", """                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                            viewModel.setCommandInput(paragraph)
                                            viewModel.switchTab(com.example.ui.TabType.CONSOLE)
                                        }""")
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
