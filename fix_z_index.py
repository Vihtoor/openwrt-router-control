import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# I will just add zIndex(1f) to the overlay Box to make sure it's above.
old_box = """                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)"""
new_box = """                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .align(Alignment.TopEnd)
                        .padding(16.dp)"""

if old_box in content:
    content = content.replace(old_box, new_box)
    print("Added zIndex")
else:
    print("Could not find overlay Box")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
