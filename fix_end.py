import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Replace the last `}}}` or whatever with correct braces.
# We'll just find the last `recognition."` and fix from there.
idx = content.rfind('recognition."')
if idx != -1:
    content = content[:idx] + 'recognition."\n            }\n            else -> ""\n        }\n    }\n}'

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
