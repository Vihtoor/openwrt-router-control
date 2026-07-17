import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target_text = """                            Text(
                                text = com.example.IperfLocalizations.getConsoleTipBody(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )"""

replacement_text = """                            Text(
                                text = if (isTv) com.example.IperfLocalizations.getConsoleTipBodyTv(locale) else com.example.IperfLocalizations.getConsoleTipBody(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )"""

content = content.replace(target_text, replacement_text)

target_arrows = """                // Left arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.CenterStart).size(width = 24.dp, height = 12.dp).offset(x = (8 + arrowOffset).dp, y = 0.dp)
                ) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        val hw = size.height / 2f
                        moveTo(0f, size.height / 2f)
                        lineTo(hw, 0f)
                        lineTo(hw, size.height * 0.35f)
                        lineTo(size.width - hw, size.height * 0.35f)
                        lineTo(size.width - hw, 0f)
                        lineTo(size.width, size.height / 2f)
                        lineTo(size.width - hw, size.height)
                        lineTo(size.width - hw, size.height * 0.65f)
                        lineTo(hw, size.height * 0.65f)
                        lineTo(hw, size.height)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
                // Right arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.CenterEnd).size(width = 24.dp, height = 12.dp).offset(x = (-8 - arrowOffset).dp, y = 0.dp)
                ) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        val hw = size.height / 2f
                        moveTo(0f, size.height / 2f)
                        lineTo(hw, 0f)
                        lineTo(hw, size.height * 0.35f)
                        lineTo(size.width - hw, size.height * 0.35f)
                        lineTo(size.width - hw, 0f)
                        lineTo(size.width, size.height / 2f)
                        lineTo(size.width - hw, size.height)
                        lineTo(size.width - hw, size.height * 0.65f)
                        lineTo(hw, size.height * 0.65f)
                        lineTo(hw, size.height)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
                // Top arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.TopCenter).size(width = 12.dp, height = 24.dp).offset(x = 0.dp, y = (8 + arrowOffset).dp)
                ) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        val hw = size.width / 2f
                        moveTo(size.width / 2f, 0f)
                        lineTo(0f, hw)
                        lineTo(size.width * 0.35f, hw)
                        lineTo(size.width * 0.35f, size.height - hw)
                        lineTo(0f, size.height - hw)
                        lineTo(size.width / 2f, size.height)
                        lineTo(size.width, size.height - hw)
                        lineTo(size.width * 0.65f, size.height - hw)
                        lineTo(size.width * 0.65f, hw)
                        lineTo(size.width, hw)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
                // Bottom arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.BottomCenter).size(width = 12.dp, height = 24.dp).offset(x = 0.dp, y = (-8 - arrowOffset).dp)
                ) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        val hw = size.width / 2f
                        moveTo(size.width / 2f, 0f)
                        lineTo(0f, hw)
                        lineTo(size.width * 0.35f, hw)
                        lineTo(size.width * 0.35f, size.height - hw)
                        lineTo(0f, size.height - hw)
                        lineTo(size.width / 2f, size.height)
                        lineTo(size.width, size.height - hw)
                        lineTo(size.width * 0.65f, size.height - hw)
                        lineTo(size.width * 0.65f, hw)
                        lineTo(size.width, hw)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }"""

replacement_arrows = """                if (!isTv) {
                    // Left arrow (vertical, on the left edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.CenterStart).size(width = 12.dp, height = 24.dp).offset(x = 2.dp, y = arrowOffset.dp)
                    ) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            val hw = size.width / 2f
                            moveTo(size.width / 2f, 0f)
                            lineTo(0f, hw)
                            lineTo(size.width * 0.35f, hw)
                            lineTo(size.width * 0.35f, size.height - hw)
                            lineTo(0f, size.height - hw)
                            lineTo(size.width / 2f, size.height)
                            lineTo(size.width, size.height - hw)
                            lineTo(size.width * 0.65f, size.height - hw)
                            lineTo(size.width * 0.65f, hw)
                            lineTo(size.width, hw)
                            close()
                        }
                        drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                    }
                    // Right arrow (vertical, on the right edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.CenterEnd).size(width = 12.dp, height = 24.dp).offset(x = (-2).dp, y = (-arrowOffset).dp)
                    ) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            val hw = size.width / 2f
                            moveTo(size.width / 2f, 0f)
                            lineTo(0f, hw)
                            lineTo(size.width * 0.35f, hw)
                            lineTo(size.width * 0.35f, size.height - hw)
                            lineTo(0f, size.height - hw)
                            lineTo(size.width / 2f, size.height)
                            lineTo(size.width, size.height - hw)
                            lineTo(size.width * 0.65f, size.height - hw)
                            lineTo(size.width * 0.65f, hw)
                            lineTo(size.width, hw)
                            close()
                        }
                        drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                    }
                    // Top arrow (horizontal, on the top edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.TopCenter).size(width = 24.dp, height = 12.dp).offset(x = arrowOffset.dp, y = 2.dp)
                    ) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            val hw = size.height / 2f
                            moveTo(0f, size.height / 2f)
                            lineTo(hw, 0f)
                            lineTo(hw, size.height * 0.35f)
                            lineTo(size.width - hw, size.height * 0.35f)
                            lineTo(size.width - hw, 0f)
                            lineTo(size.width, size.height / 2f)
                            lineTo(size.width - hw, size.height)
                            lineTo(size.width - hw, size.height * 0.65f)
                            lineTo(hw, size.height * 0.65f)
                            lineTo(hw, size.height)
                            close()
                        }
                        drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                    }
                    // Bottom arrow (horizontal, on the bottom edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.BottomCenter).size(width = 24.dp, height = 12.dp).offset(x = (-arrowOffset).dp, y = (-2).dp)
                    ) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            val hw = size.height / 2f
                            moveTo(0f, size.height / 2f)
                            lineTo(hw, 0f)
                            lineTo(hw, size.height * 0.35f)
                            lineTo(size.width - hw, size.height * 0.35f)
                            lineTo(size.width - hw, 0f)
                            lineTo(size.width, size.height / 2f)
                            lineTo(size.width - hw, size.height)
                            lineTo(size.width - hw, size.height * 0.65f)
                            lineTo(hw, size.height * 0.65f)
                            lineTo(hw, size.height)
                            close()
                        }
                        drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                    }
                }"""

content = content.replace(target_arrows, replacement_arrows)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
