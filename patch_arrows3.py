import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

target1 = """                    // Left arrow (vertical, on the left edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.CenterStart).size(width = 12.dp, height = 24.dp).offset(x = 0.dp, y = arrowOffset.dp)
                    ) {"""
replacement1 = """                    // Left arrow (vertical, on the left edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.CenterStart).size(width = 12.dp, height = 24.dp).offset(x = (-8).dp, y = arrowOffset.dp)
                    ) {"""
content = content.replace(target1, replacement1)

target2 = """                    // Right arrow (vertical, on the right edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.CenterEnd).size(width = 12.dp, height = 24.dp).offset(x = 0.dp, y = (-arrowOffset).dp)
                    ) {"""
replacement2 = """                    // Right arrow (vertical, on the right edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.CenterEnd).size(width = 12.dp, height = 24.dp).offset(x = 8.dp, y = (-arrowOffset).dp)
                    ) {"""
content = content.replace(target2, replacement2)

target3 = """                    // Top arrow (horizontal, on the top edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.TopCenter).size(width = 24.dp, height = 12.dp).offset(x = arrowOffset.dp, y = 0.dp)
                    ) {"""
replacement3 = """                    // Top arrow (horizontal, on the top edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.TopCenter).size(width = 24.dp, height = 12.dp).offset(x = arrowOffset.dp, y = (-12).dp)
                    ) {"""
content = content.replace(target3, replacement3)

target4 = """                    // Bottom arrow (horizontal, on the bottom edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.BottomCenter).size(width = 24.dp, height = 12.dp).offset(x = (-arrowOffset).dp, y = 0.dp)
                    ) {"""
replacement4 = """                    // Bottom arrow (horizontal, on the bottom edge)
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.align(Alignment.BottomCenter).size(width = 24.dp, height = 12.dp).offset(x = (-arrowOffset).dp, y = 12.dp)
                    ) {"""
content = content.replace(target4, replacement4)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
