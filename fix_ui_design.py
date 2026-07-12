import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_code = """                androidx.compose.material3.VerticalDivider(
                    modifier = Modifier.fillMaxHeight().width(1.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(headerBg)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {"""

new_code = """                androidx.compose.material3.VerticalDivider(
                    modifier = Modifier.fillMaxHeight().width(2.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(listBg)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("UI fixed")
else:
    print("Could not find the target code to replace.")
