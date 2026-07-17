import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

# Remove pending variables
content = content.replace("    private var pendingEraseSkips = 0\n    private var pendingCursorLeftSkips = 0\n", "")
content = content.replace("                pendingEraseSkips = 0\n", "")
content = content.replace("                pendingCursorLeftSkips = 0\n", "")

# Fix Erase pattern
target_erase = """                            if (isErasePattern) {
                                if (pendingEraseSkips > 0) {
                                    pendingEraseSkips--
                                } else {
                                    if (interactiveCursorIndex > 0) {
                                        val lastChar = interactiveShellOutputBuffer[interactiveCursorIndex - 1]
                                        val bytes = lastChar.toString().toByteArray(Charsets.UTF_8).size
                                        if (bytes > 1) {
                                            pendingEraseSkips += (bytes - 1)
                                        }
                                        interactiveShellOutputBuffer.deleteCharAt(interactiveCursorIndex - 1)
                                        interactiveCursorIndex--
                                    }
                                }
                                i += 3
                            } else {
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

replacement_erase = """                            if (isErasePattern) {
                                if (interactiveCursorIndex > 0) {
                                    interactiveShellOutputBuffer.deleteCharAt(interactiveCursorIndex - 1)
                                    interactiveCursorIndex--
                                }
                                i += 3
                            } else {
                                interactiveCursorIndex = maxOf(0, interactiveCursorIndex - 1)
                                i++
                            }"""

content = content.replace(target_erase, replacement_erase)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
