with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                }
            }



    if (activeHelpDialog != null) {"""

replacement = """                }
            }
        }
    }

    if (activeHelpDialog != null) {"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
