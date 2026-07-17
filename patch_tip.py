import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target1 = """    var isDismissButtonFocused by remember { mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(showConsoleInteractiveTip, isTv) {"""

replacement1 = """    var isDismissButtonFocused by remember { mutableStateOf(false) }
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    
    androidx.compose.runtime.LaunchedEffect(showConsoleInteractiveTip, isTv) {"""

content = content.replace(target1, replacement1)

target2 = """        if (showConsoleInteractiveTip && isTv) {
            kotlinx.coroutines.delay(100)
            try { focusRequesterConsoleTip.requestFocus() } catch (e: Exception) {}
        }"""

replacement2 = """        if (showConsoleInteractiveTip && isTv) {
            keyboardController?.hide()
            kotlinx.coroutines.delay(100)
            try { focusRequesterConsoleTip.requestFocus() } catch (e: Exception) {}
        }"""

content = content.replace(target2, replacement2)

target3 = """                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF9500)),
                        modifier = Modifier.widthIn(max = 300.dp).padding(16.dp)
                    ) {"""

replacement3 = """                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF9500)),
                        modifier = Modifier.widthIn(max = if (isTv) 450.dp else 300.dp).padding(16.dp)
                    ) {"""

content = content.replace(target3, replacement3)

target4 = """                                    .alpha(buttonAlpha)
                                    .onFocusChanged { isDismissButtonFocused = it.isFocused }
                                    .focusRequester(focusRequesterConsoleTip)
                            ) {"""

replacement4 = """                                    .alpha(buttonAlpha)
                                    .onFocusChanged { 
                                        isDismissButtonFocused = it.isFocused 
                                        if (!it.isFocused && isTv && showConsoleInteractiveTip) {
                                            keyboardController?.hide()
                                            try { focusRequesterConsoleTip.requestFocus() } catch (e: Exception) {}
                                        }
                                    }
                                    .focusRequester(focusRequesterConsoleTip)
                            ) {"""

content = content.replace(target4, replacement4)

with open(file_path, "w") as f:
    f.write(content)
print("Done patching tip")
