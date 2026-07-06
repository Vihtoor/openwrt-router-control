import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Replace the static call
old_call = "androidx.compose.foundation.gestures.animateScrollBy(listState, 10000f)"
new_call = "listState.animateScrollBy(10000f)"
content = content.replace(old_call, new_call)

# Add import if missing
if "import androidx.compose.foundation.gestures.animateScrollBy" not in content:
    content = content.replace("import androidx.compose.foundation.gestures.draggable",
                              "import androidx.compose.foundation.gestures.draggable\nimport androidx.compose.foundation.gestures.animateScrollBy")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
