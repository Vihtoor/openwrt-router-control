import re

file_path = "app/src/main/java/com/example/ui/RouterViewModel.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                }
                isFirstEmission = false
                if (config != null) {"""

replacement = """                }
                val wasFirstEmission = isFirstEmission
                isFirstEmission = false
                if (config != null) {"""

content = content.replace(target, replacement)

target2 = """                    if (isFirstEmission && _uiState.value.currentTab == TabType.CONSOLE && interactiveShellJob == null) {
                        startInteractiveShellSession()
                    }"""

replacement2 = """                    if (wasFirstEmission && _uiState.value.currentTab == TabType.CONSOLE && interactiveShellJob == null) {
                        startInteractiveShellSession()
                    }"""

content = content.replace(target2, replacement2)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching init 2")
