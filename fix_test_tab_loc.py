import re

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "r") as f:
    content = f.read()

# Fix package statement
content = re.sub(r'package com\..*?example\s*object TestTabLocalizations \{', 'package com.example\n\nobject TestTabLocalizations {\n', content, flags=re.DOTALL)

with open("app/src/main/java/com/example/TestTabLocalizations.kt", "w") as f:
    f.write(content)
print("done")
