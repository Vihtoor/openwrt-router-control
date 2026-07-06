import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Add draggable import if not exists
if "import androidx.compose.foundation.gestures.draggable" not in content:
    content = content.replace("import androidx.compose.foundation.gestures.scrollBy", 
                              "import androidx.compose.foundation.gestures.scrollBy\nimport androidx.compose.foundation.gestures.draggable")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
