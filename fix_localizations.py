import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Remove the incorrectly placed blocks
content = re.sub(r'            "devices" -> when \(normalizedLang\) \{\s+"ru" -> "\*\*Список.*?\}\n', '', content, flags=re.DOTALL)
content = re.sub(r'            "devices" -> when \(normalizedLang\) \{\s+"ru" -> "Устройства".*?\}\n', '', content, flags=re.DOTALL)

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
print("Removed old ones.")

