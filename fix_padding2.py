import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_box = """            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                    .padding(top = 60.dp, bottom = 60.dp),
                contentAlignment = Alignment.Center
            ) {"""

new_box = """            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                    .padding(top = 0.dp, bottom = 120.dp),
                contentAlignment = Alignment.Center
            ) {"""

if old_box in content:
    content = content.replace(old_box, new_box)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Success")
else:
    print("Not found")
