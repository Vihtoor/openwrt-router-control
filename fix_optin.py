import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

opt_in_str = "@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)\n"
if opt_in_str not in content:
    content = opt_in_str + content

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
