import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "private var pendingEraseSkips = 0",
    "private var pendingEraseSkips = 0\n    private var pendingCursorLeftSkips = 0"
)

content = content.replace(
    "pendingEraseSkips = 0",
    "pendingEraseSkips = 0\n                pendingCursorLeftSkips = 0"
)

target = """                            } else {
                                interactiveCursorIndex = maxOf(0, interactiveCursorIndex - 1)
                                i++
                            }"""

replacement = """                            } else {
                                if (pendingCursorLeftSkips > 0) {
                                    pendingCursorLeftSkips--
                                } else {
                                    if (interactiveCursorIndex > 0) {
                                        val skippedChar = interactiveShellOutputBuffer[interactiveCursorIndex - 1]
                                        val bytes = skippedChar.toString().toByteArray(Charsets.UTF_8).size
                                        if (bytes > 1) {
                                            pendingCursorLeftSkips += (bytes - 1)
                                        }
                                        interactiveCursorIndex--
                                    }
                                }
                                i++
                            }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
