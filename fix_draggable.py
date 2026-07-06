import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Remove draggable block 1
old_draggable_1 = """                    .draggable(
                        orientation = androidx.compose.foundation.gestures.Orientation.Vertical,
                        state = androidx.compose.foundation.gestures.rememberDraggableState { delta ->
                            coroutineScope.launch {
                                listState.scrollBy(-delta)
                            }
                        }
                    )"""
content = content.replace(old_draggable_1, "")

# 2. Remove draggable block 2
old_draggable_2 = """                .draggable(
                    orientation = androidx.compose.foundation.gestures.Orientation.Vertical,
                    state = androidx.compose.foundation.gestures.rememberDraggableState { delta ->
                        coroutineScope.launch {
                            listState.scrollBy(-delta)
                        }
                    }
                )"""
content = content.replace(old_draggable_2, "")

# 3. Change the bottom spacer in EditRouterForm
old_bottom_spacer = """            item {
                Spacer(modifier = Modifier.height(20.dp))
            }"""
new_bottom_spacer = """            item {
                Spacer(modifier = if (isTv) Modifier.height(1.dp) else Modifier.height(20.dp))
            }"""
content = content.replace(old_bottom_spacer, new_bottom_spacer)

# 4. Remove checkConnectionFocusRequester and its usages
content = content.replace("    val checkConnectionFocusRequester = remember { FocusRequester() }\n", "")
content = content.replace("keyboardActions = KeyboardActions(onDone = { checkConnectionFocusRequester.requestFocus() }),", "keyboardActions = KeyboardActions(onDone = { }),")
content = content.replace(".focusRequester(checkConnectionFocusRequester)", "")

# 5. Make sure keyboardActions is completely removed or safe
# Wait, KeyboardActions(onDone = { }) is fine. Or remove the whole line.
content = content.replace("keyboardActions = KeyboardActions(onDone = { }),\n", "")

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
