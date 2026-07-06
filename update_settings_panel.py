import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

imports = """
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.draw.scale
"""

if "import androidx.compose.ui.platform.LocalConfiguration" not in content:
    content = content.replace("import androidx.compose.ui.platform.LocalContext", imports + "\nimport androidx.compose.ui.platform.LocalContext")

function_start = content.find("fun SettingsPanel(")
if function_start != -1:
    body_start = content.find("{", function_start) + 1
    
    wrapper_code = """
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTv = remember { context.packageManager.hasSystemFeature("android.software.leanback") }
    val isTablet = configuration.screenWidthDp >= 600
    val scaleModifier = if (isTv || isTablet) Modifier.fillMaxSize().scale(0.5f) else Modifier.fillMaxSize()
    
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = scaleModifier) {
"""
    
    body_end = content.find("}\n\n@Composable\nfun RouterProfilesList", body_start)
    if body_end != -1:
        original_body = content[body_start:body_end]
        new_body = wrapper_code + original_body + "        }\n    }"
        content = content[:body_start] + new_body + content[body_end:]
        
        with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
            f.write(content)
        print("SettingsPanel updated successfully")
    else:
        print("Could not find end of SettingsPanel")
else:
    print("Could not find SettingsPanel function")
