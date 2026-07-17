import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val sharedPrefs = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var showConsoleInteractiveTip by remember { mutableStateOf(sharedPrefs.getBoolean("show_console_tip_v1", true)) }
    val focusRequesterConsoleTip = remember { androidx.compose.ui.focus.FocusRequester() }
    var isDismissButtonFocused by remember { mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(showConsoleInteractiveTip, isTv) {
        if (showConsoleInteractiveTip && isTv) {
            kotlinx.coroutines.delay(100)
            try { focusRequesterConsoleTip.requestFocus() } catch (e: Exception) {}
        }
    }"""

original = """    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }"""

# Replace all occurrences with original, then just put it back once inside ConsoleTab
content = content.replace(target, original)

# Now, we know ConsoleTab starts around "fun ConsoleTab(". We will find the one inside ConsoleTab
console_tab_index = content.find("fun ConsoleTab(")
if console_tab_index != -1:
    end_of_isTv_in_consoleTab = content.find(original, console_tab_index)
    if end_of_isTv_in_consoleTab != -1:
        # replace just this one
        content = content[:end_of_isTv_in_consoleTab] + target + content[end_of_isTv_in_consoleTab + len(original):]

with open(file_path, "w") as f:
    f.write(content)
print("Done")
