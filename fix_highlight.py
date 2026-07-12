import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_code = """                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { focusedDeviceMac = device.mac }
                                        .onFocusChanged { 
                                            isFocused = it.isFocused
                                            if (it.isFocused) {
                                                focusedDeviceMac = device.mac
                                            }
                                        }
                                        .focusable()
                                        .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(vertical = 12.dp, horizontal = 12.dp),"""

new_code = """                                        .fillMaxWidth()
                                        .clickable { focusedDeviceMac = device.mac }
                                        .onFocusChanged { 
                                            isFocused = it.isFocused
                                            if (it.isFocused) {
                                                focusedDeviceMac = device.mac
                                            }
                                        }
                                        .focusable()
                                        .padding(vertical = 7.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isFocused || isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(vertical = 5.dp, horizontal = 12.dp),"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Highlight padding fixed")
else:
    print("Could not find the target code to replace.")
