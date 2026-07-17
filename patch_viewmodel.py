import re

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

# Add interactiveCursorIndex
replacement1 = """    private val interactiveShellOutputBuffer = StringBuilder()
    private var interactiveCursorIndex = 0
"""
content = re.sub(r'private val interactiveShellOutputBuffer = StringBuilder\(\)\n', replacement1, content)

# Find the startInteractiveShell chunk
start = content.find('repository.startInteractiveShell(config) { chunk ->')
end = content.find('} catch (e: Exception) {', start)

new_chunk = """repository.startInteractiveShell(config) { chunk ->
                    val cleanedChunk = chunk.replace(Regex("\\x1b?\\]0;.*?\\x07"), "")
                    
                    var i = 0
                    val len = cleanedChunk.length
                    while (i < len) {
                        val char = cleanedChunk[i]
                        if (char == '\\b' || char == '\\u0008' || char == '\\u007F' || char == '\\u007f') {
                            val isErasePattern = i + 2 < len && 
                                                 cleanedChunk[i + 1] == ' ' && 
                                                 (cleanedChunk[i + 2] == '\\b' || cleanedChunk[i + 2] == '\\u0008' || cleanedChunk[i + 2] == '\\u007F' || cleanedChunk[i + 2] == '\\u007f')
                            if (isErasePattern) {
                                interactiveCursorIndex = maxOf(0, interactiveCursorIndex - 1)
                                i += 3
                            } else {
                                interactiveCursorIndex = maxOf(0, interactiveCursorIndex - 1)
                                i++
                            }
                        } else if (char == '\\r') {
                            val lastN = interactiveShellOutputBuffer.lastIndexOf("\\n", interactiveCursorIndex - 1)
                            interactiveCursorIndex = maxOf(0, lastN + 1)
                            i++
                        } else if (char == '\\n') {
                            if (interactiveCursorIndex == interactiveShellOutputBuffer.length) {
                                interactiveShellOutputBuffer.append(char)
                                interactiveCursorIndex++
                            } else {
                                val nextN = interactiveShellOutputBuffer.indexOf("\\n", interactiveCursorIndex)
                                if (nextN == -1) {
                                    interactiveShellOutputBuffer.append(char)
                                    interactiveCursorIndex = interactiveShellOutputBuffer.length
                                } else {
                                    interactiveCursorIndex = nextN + 1
                                }
                            }
                            i++
                        } else if (char == '\\u001b' || char == '\\u001B') {
                            if (i + 1 < len && cleanedChunk[i + 1] == '[') {
                                var j = i + 2
                                while (j < len && cleanedChunk[j] !in 'a'..'z' && cleanedChunk[j] !in 'A'..'Z') {
                                    j++
                                }
                                if (j < len) {
                                    val cmd = cleanedChunk[j]
                                    val seq = cleanedChunk.substring(i, j + 1)
                                    if (cmd == 'm') {
                                        if (interactiveCursorIndex == interactiveShellOutputBuffer.length) {
                                            interactiveShellOutputBuffer.append(seq)
                                            interactiveCursorIndex += seq.length
                                        } else {
                                            interactiveShellOutputBuffer.insert(interactiveCursorIndex, seq)
                                            interactiveCursorIndex += seq.length
                                        }
                                    } else if (cmd == 'D') {
                                        val paramStr = cleanedChunk.substring(i + 2, j)
                                        val count = paramStr.toIntOrNull() ?: 1
                                        interactiveCursorIndex = maxOf(0, interactiveCursorIndex - count)
                                    } else if (cmd == 'C') {
                                        val paramStr = cleanedChunk.substring(i + 2, j)
                                        val count = paramStr.toIntOrNull() ?: 1
                                        interactiveCursorIndex = minOf(interactiveShellOutputBuffer.length, interactiveCursorIndex + count)
                                    } else if (cmd == 'K') {
                                        val paramStr = cleanedChunk.substring(i + 2, j)
                                        if (paramStr.isEmpty() || paramStr == "0") {
                                            val nextN = interactiveShellOutputBuffer.indexOf("\\n", interactiveCursorIndex)
                                            val endIdx = if (nextN == -1) interactiveShellOutputBuffer.length else nextN
                                            if (endIdx > interactiveCursorIndex) {
                                                interactiveShellOutputBuffer.delete(interactiveCursorIndex, endIdx)
                                            }
                                        } else if (paramStr == "1") {
                                            val lastN = interactiveShellOutputBuffer.lastIndexOf("\\n", interactiveCursorIndex - 1)
                                            val startIdx = maxOf(0, lastN + 1)
                                            if (interactiveCursorIndex > startIdx) {
                                                interactiveShellOutputBuffer.delete(startIdx, interactiveCursorIndex)
                                                interactiveCursorIndex = startIdx
                                            }
                                        } else if (paramStr == "2") {
                                            val lastN = interactiveShellOutputBuffer.lastIndexOf("\\n", interactiveCursorIndex - 1)
                                            val startIdx = maxOf(0, lastN + 1)
                                            val nextN = interactiveShellOutputBuffer.indexOf("\\n", interactiveCursorIndex)
                                            val endIdx = if (nextN == -1) interactiveShellOutputBuffer.length else nextN
                                            if (endIdx > startIdx) {
                                                interactiveShellOutputBuffer.delete(startIdx, endIdx)
                                                interactiveCursorIndex = startIdx
                                            }
                                        }
                                    } else if (cmd == 'J') {
                                        val paramStr = cleanedChunk.substring(i + 2, j)
                                        if (paramStr.isEmpty() || paramStr == "0") {
                                            if (interactiveCursorIndex < interactiveShellOutputBuffer.length) {
                                                interactiveShellOutputBuffer.delete(interactiveCursorIndex, interactiveShellOutputBuffer.length)
                                            }
                                        } else if (paramStr == "2") {
                                            interactiveShellOutputBuffer.setLength(0)
                                            interactiveCursorIndex = 0
                                        }
                                    } else if (cmd == 'A' || cmd == 'B' || cmd == 'G' || cmd == 'H' || cmd == 'f') {
                                        // Ignore these vertical movements for now
                                    } else if (cmd == 'P') { // Delete character
                                        val paramStr = cleanedChunk.substring(i + 2, j)
                                        val count = paramStr.toIntOrNull() ?: 1
                                        val nextN = interactiveShellOutputBuffer.indexOf("\\n", interactiveCursorIndex)
                                        val endIdx = if (nextN == -1) interactiveShellOutputBuffer.length else nextN
                                        val actualCount = minOf(count, endIdx - interactiveCursorIndex)
                                        if (actualCount > 0) {
                                            interactiveShellOutputBuffer.delete(interactiveCursorIndex, interactiveCursorIndex + actualCount)
                                        }
                                    }
                                    i = j + 1
                                } else {
                                    if (interactiveCursorIndex == interactiveShellOutputBuffer.length) {
                                        interactiveShellOutputBuffer.append(char)
                                    } else {
                                        interactiveShellOutputBuffer.setCharAt(interactiveCursorIndex, char)
                                    }
                                    interactiveCursorIndex++
                                    i++
                                }
                            } else {
                                if (interactiveCursorIndex == interactiveShellOutputBuffer.length) {
                                    interactiveShellOutputBuffer.append(char)
                                } else {
                                    interactiveShellOutputBuffer.setCharAt(interactiveCursorIndex, char)
                                }
                                interactiveCursorIndex++
                                i++
                            }
                        } else {
                            if (interactiveCursorIndex == interactiveShellOutputBuffer.length) {
                                interactiveShellOutputBuffer.append(char)
                            } else {
                                interactiveShellOutputBuffer.setCharAt(interactiveCursorIndex, char)
                            }
                            interactiveCursorIndex++
                            i++
                        }
                    }
                    val currentLen = interactiveShellOutputBuffer.length
                    if (currentLen > 50000) {
                        val deletedCount = currentLen - 50000
                        interactiveShellOutputBuffer.delete(0, deletedCount)
                        interactiveCursorIndex = maxOf(0, interactiveCursorIndex - deletedCount)
                    }
                    viewModelScope.launch {
                        interactiveShellLogId?.let { lid ->
                            repository.updateConsoleLog(lid, "sh", interactiveShellOutputBuffer.toString())
                        }
                    }
                }
            """

content = content[:start] + new_chunk + content[end:]

# Set interactiveCursorIndex to 0 when initializing
init_start = content.find('interactiveShellOutputBuffer.setLength(0)')
content = content[:init_start] + "interactiveCursorIndex = 0\n                " + content[init_start:]

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
