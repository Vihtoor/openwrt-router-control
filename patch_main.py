import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                                if (kotlin.math.abs(accumulatedX) > kotlin.math.abs(accumulatedY)) {
                                    if (accumulatedX > 0) {
                                        highlightedDirection = "RIGHT"
                                        if (currentTypedLine.isNotEmpty()) {
                                            onWriteRawToConsoleStdin("\\u001B[C")
                                        }
                                    } else {
                                        highlightedDirection = "LEFT"
                                        if (currentTypedLine.isNotEmpty()) {
                                            onWriteRawToConsoleStdin("\\u001B[D")
                                        }
                                    }
                                } else {
                                    if (accumulatedY > 0) {
                                        highlightedDirection = "DOWN"
                                        if (sessionCommandHistory.isNotEmpty()) {
                                            if (historyIndex < sessionCommandHistory.size - 1 && historyIndex != -1) {
                                                historyIndex++
                                                val cmd = sessionCommandHistory[historyIndex]
                                                val bytesToDelete = currentTypedLine.toByteArray(Charsets.UTF_8).size
                                                repeat(bytesToDelete) { onWriteRawToConsoleStdin("\\u007F") }
                                                cmd.forEach { char -> onWriteRawToConsoleStdin(char.toString()) }
                                                currentTypedLine = cmd
                                            } else if (historyIndex == sessionCommandHistory.size - 1) {
                                                historyIndex = -1
                                                val bytesToDelete = currentTypedLine.toByteArray(Charsets.UTF_8).size
                                                repeat(bytesToDelete) { onWriteRawToConsoleStdin("\\u007F") }
                                                currentTypedLine = ""
                                            }
                                        }
                                    } else {
                                        highlightedDirection = "UP"
                                        if (sessionCommandHistory.isNotEmpty()) {
                                            if (historyIndex == -1) {
                                                historyIndex = sessionCommandHistory.size - 1
                                            } else if (historyIndex > 0) {
                                                historyIndex--
                                            }
                                            val cmd = sessionCommandHistory[historyIndex]
                                            val bytesToDelete = currentTypedLine.toByteArray(Charsets.UTF_8).size
                                            repeat(bytesToDelete) { onWriteRawToConsoleStdin("\\u007F") }
                                            cmd.forEach { char -> onWriteRawToConsoleStdin(char.toString()) }
                                            currentTypedLine = cmd
                                        }
                                    }
                                }"""

replacement = """                                if (kotlin.math.abs(accumulatedX) > kotlin.math.abs(accumulatedY)) {
                                    if (accumulatedX > 0) {
                                        highlightedDirection = "RIGHT"
                                        onWriteRawToConsoleStdin("\\u001B[C")
                                    } else {
                                        highlightedDirection = "LEFT"
                                        onWriteRawToConsoleStdin("\\u001B[D")
                                    }
                                } else {
                                    if (accumulatedY > 0) {
                                        highlightedDirection = "DOWN"
                                        onWriteRawToConsoleStdin("\\u001B[B")
                                    } else {
                                        highlightedDirection = "UP"
                                        onWriteRawToConsoleStdin("\\u001B[A")
                                    }
                                }"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Success")
else:
    print("Failed to find target")
