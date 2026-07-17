import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                    view.onDeleteSurroundingText = { before, _ ->
                        val textToDelete = currentTypedLine.takeLast(before)
                        val bytesToDelete = textToDelete.toByteArray(Charsets.UTF_8).size
                        repeat(bytesToDelete) {
                            onWriteRawToConsoleStdin("\\u007F")
                        }
                        if (currentTypedLine.isNotEmpty()) {
                            val actualBefore = minOf(before, currentTypedLine.length)
                            currentTypedLine = currentTypedLine.dropLast(actualBefore)
                        }
                    }"""

replacement = """                    view.onDeleteSurroundingText = { before, _ ->
                        // Calculate bytes using the actual character before cursor in the shell buffer
                        var bytesToDelete = before
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
                        }
                        
                        if (currentTypedLine.isNotEmpty()) {
                            val actualBefore = minOf(before, currentTypedLine.length)
                            currentTypedLine = currentTypedLine.dropLast(actualBefore)
                        }
                    }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
