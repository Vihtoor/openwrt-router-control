import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

old_code = """                    val cleanedChunk = chunk.replace("]0;root@OpenWrt:", "")
                        .replace("\\u001b]0;root@OpenWrt:\\u0007", "")
                        .replace("\\u001B]0;root@OpenWrt:\\u0007", "")"""

new_code = """                    val cleanedChunk = chunk.replace(Regex("\\\\x1b?\\\\]0;.*?\\\\x07"), "")"""

if old_code in content:
    content = content.replace(old_code, new_code)
    print("Fixed RouterViewModel")
else:
    print("RouterViewModel code not found")

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)

