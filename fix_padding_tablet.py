import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_code = """                                        .focusable()
                                        .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(vertical = 16.dp, horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,"""

new_code = """                                        .focusable()
                                        .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(vertical = 12.dp, horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Padding fixed")
else:
    print("Could not find the target code to replace.")
