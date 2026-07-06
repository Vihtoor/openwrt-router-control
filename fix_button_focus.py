import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_test_btn = """                    Button(
                        onClick = {
                            val p = port.toIntOrNull() ?: 22
                            onTest(ip, p, user, password)
                        },
                        interactionSource = testInteractionSource,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isConnecting,
                        contentPadding = PaddingValues(0.dp),"""

new_test_btn = """                    Button(
                        onClick = {
                            if (!isConnecting) {
                                val p = port.toIntOrNull() ?: 22
                                onTest(ip, p, user, password)
                            }
                        },
                        interactionSource = testInteractionSource,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = true,
                        contentPadding = PaddingValues(0.dp),"""

content = content.replace(old_test_btn, new_test_btn)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
