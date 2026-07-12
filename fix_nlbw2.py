import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_cond = """if (idx == 0 && line.contains("rx_bytes", ignoreCase = true) || line.contains("rx", ignoreCase = true)) {"""
new_cond = """if (idx == 0 && (line.contains("rx_bytes", ignoreCase = true) || line.contains("rx", ignoreCase = true))) {"""

if old_cond in content:
    content = content.replace(old_cond, new_cond)
    with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
        f.write(content)
    print("Fixed precedence")
else:
    print("Not found")
