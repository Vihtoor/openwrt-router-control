import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r', encoding='utf-8') as f:
    content = f.read()

target = '''                    val scrollState = androidx.compose.foundation.rememberScrollState()
                    Column(modifier = Modifier.weight(1f, fill = false).verticalScroll(scrollState)) {'''

replacement = '''                    val scrollState = androidx.compose.foundation.rememberScrollState()
                    val interactionSource = androidx.compose.runtime.remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    
                    androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f, fill = false)) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .verticalScroll(scrollState)
                            .focusable(interactionSource = interactionSource)
                        ) {'''

content = content.replace(target, replacement)

target2 = '''                            Spacer(modifier = Modifier.height(4.dp))
                            Text("- " + translateText("Проверить обновление можно вручную в разделе настроек О приложении", context), style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color(0xFF4CAF50))
                    }
                }'''

replacement2 = '''                            Spacer(modifier = Modifier.height(4.dp))
                            Text("- " + translateText("Проверить обновление можно вручную в разделе настроек О приложении", context), style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color(0xFF4CAF50))
                        }
                        
                        val showScrollbar = scrollState.maxValue > 0
                        if (showScrollbar) {
                            val fraction = if (scrollState.maxValue == 0) 0f else scrollState.value.toFloat() / scrollState.maxValue
                            androidx.compose.foundation.Canvas(modifier = Modifier.align(Alignment.CenterEnd).width(4.dp).fillMaxHeight()) {
                                drawRoundRect(
                                    color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.3f),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                                )
                                val thumbHeight = 40.dp.toPx()
                                val maxThumbOffset = size.height - thumbHeight
                                val thumbOffset = fraction * maxThumbOffset
                                drawRoundRect(
                                    color = androidx.compose.ui.graphics.Color.Gray,
                                    topLeft = androidx.compose.ui.geometry.Offset(0f, thumbOffset),
                                    size = androidx.compose.ui.geometry.Size(size.width, thumbHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                                )
                            }
                        }
                    }
                }'''

content = content.replace(target2, replacement2)

with open('app/src/main/java/com/example/MainActivity.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("MainActivity patched")
