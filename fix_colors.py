import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_colors = """            val listBg = MaterialTheme.colorScheme.surface
            val headerBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)"""

new_colors = """            val listBg = MaterialTheme.colorScheme.surface
            val headerBg = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.08f)"""

content = content.replace(old_colors, new_colors)

old_divider = """                                        androidx.compose.material3.Divider(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                                            thickness = 1.dp
                                        )"""

new_divider = """                                        androidx.compose.material3.HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            color = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.12f),
                                            thickness = 1.dp
                                        )"""

content = content.replace(old_divider, new_divider)
content = content.replace("androidx.compose.material3.Divider", "androidx.compose.material3.HorizontalDivider")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

