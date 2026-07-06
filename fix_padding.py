import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_padding = "                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 20.dp)"
new_padding = "                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = if (isTv) 0.dp else 24.dp)"
content = content.replace(old_padding, new_padding)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
