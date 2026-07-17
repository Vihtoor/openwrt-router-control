import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                                if (kotlin.math.abs(accumulatedX) > kotlin.math.abs(accumulatedY)) {
                                    if (accumulatedX > 0) {
                                        highlightedDirection = "RIGHT"
                                        onWriteRawToConsoleStdin("\\u001B[C")
                                    } else {
                                        highlightedDirection = "LEFT"
                                        onWriteRawToConsoleStdin("\\u001B[D")
                                    }
                                }"""

replacement = """                                if (kotlin.math.abs(accumulatedX) > kotlin.math.abs(accumulatedY)) {
                                    if (accumulatedX > 0) {
                                        highlightedDirection = "RIGHT"
                                        var bytesToMove = 1
                                        val shLog = consoleHistory.firstOrNull { it.command == "sh" }
                                        if (shLog != null) {
                                            val out = shLog.output
                                            val idx = interactiveCursorIndex
                                            if (idx >= 0 && idx < out.length) {
                                                val charToSkip = out[idx]
                                                bytesToMove = charToSkip.toString().toByteArray(Charsets.UTF_8).size
                                            }
                                        }
                                        repeat(bytesToMove) {
                                            onWriteRawToConsoleStdin("\\u001B[C")
                                        }
                                    } else {
                                        highlightedDirection = "LEFT"
                                        var bytesToMove = 1
                                        val shLog = consoleHistory.firstOrNull { it.command == "sh" }
                                        if (shLog != null) {
                                            val out = shLog.output
                                            val idx = interactiveCursorIndex
                                            if (idx > 0 && idx <= out.length) {
                                                val charToSkip = out[idx - 1]
                                                bytesToMove = charToSkip.toString().toByteArray(Charsets.UTF_8).size
                                            }
                                        }
                                        repeat(bytesToMove) {
                                            onWriteRawToConsoleStdin("\\u001B[D")
                                        }
                                    }
                                }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
