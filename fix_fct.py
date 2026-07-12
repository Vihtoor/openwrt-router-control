with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

import re
content = re.sub(r'(\w+)\.formattedConnectionType\b', r'\1.formattedConnectionType(context)', content)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
