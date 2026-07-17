import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

target = """                        if (char == '\\b' || char == '\\u0008' || char == '\\u007F' || char == '\\u007f') {
                            val isErasePattern = i + 2 < len && 
                                                 cleanedChunk[i + 1] == ' ' && 
                                                 (cleanedChunk[i + 2] == '\\b' || cleanedChunk[i + 2] == '\\u0008' || cleanedChunk[i + 2] == '\\u007F' || cleanedChunk[i + 2] == '\\u007f')
                            if (isErasePattern) {
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
                                i++
                            }
                        }"""

replacement = """                        if (char == '\\b' || char == '\\u0008' || char == '\\u007F' || char == '\\u007f') {
                            val isErasePattern = i + 2 < len && 
                                                 cleanedChunk[i + 1] == ' ' && 
                                                 (cleanedChunk[i + 2] == '\\b' || cleanedChunk[i + 2] == '\\u0008' || cleanedChunk[i + 2] == '\\u007F' || cleanedChunk[i + 2] == '\\u007f')
                            if (isErasePattern) {
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
                                interactiveCursorIndex = maxOf(0, interactiveCursorIndex - 1)
                                i++
                            }
                        }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
