import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_scroll = """                                    val lastIndex = listState.layoutInfo.totalItemsCount - 1
                                    if (lastIndex >= 0) {
                                        listState.animateScrollToItem(lastIndex)
                                    }"""
new_scroll = """                                    androidx.compose.foundation.gestures.animateScrollBy(listState, 10000f)"""
content = content.replace(old_scroll, new_scroll)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
