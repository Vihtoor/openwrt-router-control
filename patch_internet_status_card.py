with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("""                onNavigateToConsole = {
                    showDevices = false
                    onNavigateToConsole()
                }""", """                onNavigateToConsole = { cmd ->
                    showDevices = false
                    onNavToConsole(cmd)
                }""")
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
