fun main() {
    val interactiveShellOutputBuffer = StringBuilder("root@OpenWrt:~# abc")
    val inputs = listOf("\b \b", "\b \b")
    for (cleanedChunk in inputs) {
        var i = 0
        val len = cleanedChunk.length
        while (i < len) {
            val char = cleanedChunk[i]
            if (char == '\b' || char == '\u0008' || char == '\u007F' || char == '\u007f') {
                val isErasePattern = i + 2 < len && 
                                     cleanedChunk[i + 1] == ' ' && 
                                     (cleanedChunk[i + 2] == '\b' || cleanedChunk[i + 2] == '\u0008' || cleanedChunk[i + 2] == '\u007F' || cleanedChunk[i + 2] == '\u007f')
                if (isErasePattern) {
                    if (interactiveShellOutputBuffer.isNotEmpty()) {
                        interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                    }
                    i += 3
                } else {
                    if (interactiveShellOutputBuffer.isNotEmpty()) {
                        interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                    }
                    i++
                }
            } else if (char == '\u001b' || char == '\u001B') {
                if (i + 2 < len && cleanedChunk[i + 1] == '[' && cleanedChunk[i + 2] == 'D') {
                    if (interactiveShellOutputBuffer.isNotEmpty()) {
                        interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                    }
                    i += 3
                } else if (i + 3 < len && cleanedChunk[i + 1] == '[' && cleanedChunk[i + 2] == '1' && cleanedChunk[i + 3] == 'D') {
                    if (interactiveShellOutputBuffer.isNotEmpty()) {
                        interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                    }
                    i += 4
                } else if (i + 2 < len && cleanedChunk[i + 1] == '[' && cleanedChunk[i + 2] == 'K') {
                    i += 3
                } else {
                    interactiveShellOutputBuffer.append(char)
                    i++
                }
            } else {
                interactiveShellOutputBuffer.append(char)
                i++
            }
        }
        println("Buffer after chunk: '$interactiveShellOutputBuffer'")
    }
}
