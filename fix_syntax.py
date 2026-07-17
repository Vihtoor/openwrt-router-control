import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
"""    private var pendingEraseSkips = 0
                pendingCursorLeftSkips = 0
    private var pendingCursorLeftSkips = 0""",
"""    private var pendingEraseSkips = 0
    private var pendingCursorLeftSkips = 0"""
)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
