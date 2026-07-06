import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

old_lc = """            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {"""

new_lc = """            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                listState.scrollBy(-dragAmount)
                            }
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {"""

content = content.replace(old_lc, new_lc)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
