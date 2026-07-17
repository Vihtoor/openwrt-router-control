import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace(
    'onDragStart = { offset ->',
    'onDragStart = { offset: androidx.compose.ui.geometry.Offset ->'
)
content = content.replace(
    'onDrag = { change, dragAmount ->',
    'onDrag = { change: androidx.compose.ui.input.pointer.PointerInputChange, dragAmount: androidx.compose.ui.geometry.Offset ->'
)
content = content.replace(
    'onTap = {',
    'onTap = { offset: androidx.compose.ui.geometry.Offset ->'
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
