import re

file_path = "app/src/main/java/com/example/MainActivity.kt"
with open(file_path, "r") as f:
    content = f.read()

target1 = """                modifier = Modifier.focusRequester(terminalWindowFocusRequester).focusProperties {
                    up = clearButtonFocusRequester
                    down = termKeysFocusRequester
                },"""
content = content.replace(target1, "")

target2 = """                },
                modifier = Modifier.fillMaxSize()
            )"""
replacement2 = """                },
                modifier = Modifier.fillMaxSize().focusRequester(terminalWindowFocusRequester).focusProperties {
                    up = clearButtonFocusRequester
                    down = termKeysFocusRequester
                }
            )"""
content = content.replace(target2, replacement2)

with open(file_path, "w") as f:
    f.write(content)
print("Modifier merged!")
