import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_card = """            Card(
                modifier = Modifier
                    .width(360.dp)
                    .padding(vertical = 16.dp),"""

new_card = """            Card(
                modifier = Modifier
                    .width(360.dp)
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                    .padding(vertical = 16.dp),"""

content = content.replace(old_card, new_card)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
