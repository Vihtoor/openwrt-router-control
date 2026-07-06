import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

# Replace pointerInput with draggable
old_pointer_input = """                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                listState.scrollBy(-dragAmount)
                            }
                        }
                    }"""

new_draggable = """                    .draggable(
                        orientation = androidx.compose.foundation.gestures.Orientation.Vertical,
                        state = androidx.compose.foundation.gestures.rememberDraggableState { delta ->
                            coroutineScope.launch {
                                listState.scrollBy(-delta)
                            }
                        }
                    )"""

content = content.replace(old_pointer_input, new_draggable)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
