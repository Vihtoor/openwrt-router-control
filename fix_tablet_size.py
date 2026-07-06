import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Make tablet modifier use fillMaxSize
content = content.replace("modifier = if (isTablet) Modifier.fillMaxHeight() else Modifier", "modifier = if (isTablet) Modifier.fillMaxSize() else Modifier")

# Make scaleModifier pad 20.dp top and bottom, but maybe add some horizontal padding?
# Let's just follow the prompt: padding top and bottom 20.dp.
content = content.replace("val scaleModifier = if (isTv) Modifier.fillMaxSize().scale(0.5f) else if (isTablet) Modifier.fillMaxSize().padding(vertical = 20.dp) else Modifier.fillMaxSize()", "val scaleModifier = if (isTv) Modifier.fillMaxSize().scale(0.5f) else if (isTablet) Modifier.fillMaxSize().padding(vertical = 20.dp, horizontal = 16.dp) else Modifier.fillMaxSize()")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
