import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace(
    'onDrag = { change: androidx.compose.ui.input.pointer.PointerInputChange',
    'onDrag = { change '
).replace(
    'dragAmount: androidx.compose.ui.geometry.Offset ->',
    'dragAmount ->'
).replace(
    'change.consume()',
    'change.consume()' # I will fix this!
)

# wait, consume() is an extension on PointerInputChange which requires androidx.compose.ui.input.pointer.consumeAllChanges() or something
# in newer compose versions, consume() is on PointerInputChange. But if PointerInputChange is not imported, extension function might not be visible.
# Let's import PointerInputChange and see if consume() resolves. Or just import androidx.compose.ui.input.pointer.*

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
