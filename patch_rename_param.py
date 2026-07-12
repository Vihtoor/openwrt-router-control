with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("fun InternetStatusCard(state: UiState, modifier: Modifier = Modifier, onRefreshStatus: (() -> Unit)? = null, onNavToConsole: (String?) -> Unit = {})",
                          "fun InternetStatusCard(state: UiState, modifier: Modifier = Modifier, onRefreshStatus: (() -> Unit)? = null, onNavigateToConsole: (String?) -> Unit = {})")

content = content.replace("""                onNavigateToConsole = { cmd ->
                    showDevices = false
                    onNavToConsole(cmd)
                }""", """                onNavigateToConsole = { cmd ->
                    showDevices = false
                    onNavigateToConsole(cmd)
                }""")

# Also in DashboardTab calling InternetStatusCard
content = content.replace("""                        onNavToConsole = onNavigateToConsole""", """                        onNavigateToConsole = onNavigateToConsole""")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
