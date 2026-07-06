import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Add focusable
content = content.replace(".onFocusChanged { isEditFocused = it.isFocused }", ".focusable()\n                                    .onFocusChanged { isEditFocused = it.isFocused }")
content = content.replace(".onFocusChanged { isDeleteFocused = it.isFocused }", ".focusable()\n                                    .onFocusChanged { isDeleteFocused = it.isFocused }")
content = content.replace(".onFocusChanged { isIconFocused = it.isFocused }", ".focusable()\n                            .onFocusChanged { isIconFocused = it.isFocused }")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
