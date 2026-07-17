import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """                            }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { onApplyVpnChanges() },"""

replacement = """                            }
                        }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { onApplyVpnChanges() },"""

content = content.replace(target, replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
