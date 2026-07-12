with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

import re

match = re.search(r'(            // Wi-Fi Analyzer Card\n.*?(?:            // Beautiful M3 iPerf3 Card below M-Lab\n).*?                    }\n                }\n            })', content, flags=re.DOTALL)

if match:
    print("Found cards")
else:
    print("Cards not found")
