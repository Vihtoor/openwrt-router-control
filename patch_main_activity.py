import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Fix bytesToDelete
target1 = """                        var bytesToDelete = before
                        val shLog = consoleHistory.firstOrNull { it.command == "sh" }
                        if (shLog != null) {
                            val out = shLog.output
                            val idx = interactiveCursorIndex
                            if (idx > 0 && idx <= out.length) {
                                val charToDelete = out[idx - 1]
                                bytesToDelete = charToDelete.toString().toByteArray(Charsets.UTF_8).size
                            }
                        }
                        
                        repeat(bytesToDelete) {
                            onWriteRawToConsoleStdin("\\u007F")
                        }"""

replacement1 = """                        repeat(before) {
                            onWriteRawToConsoleStdin("\\u007F")
                        }"""

content = content.replace(target1, replacement1)

# Fix bytesToMove RIGHT
target2 = """                                        var bytesToMove = 1
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
                                        }"""

replacement2 = """                                        onWriteRawToConsoleStdin("\\u001B[C")"""

content = content.replace(target2, replacement2)

# Fix bytesToMove LEFT
target3 = """                                        var bytesToMove = 1
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
                                        }"""

replacement3 = """                                        onWriteRawToConsoleStdin("\\u001B[D")"""

content = content.replace(target3, replacement3)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
