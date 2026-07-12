import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

old_nlbw = """        val nlbwRx = mutableMapOf<String, Long>()
        val nlbwTx = mutableMapOf<String, Long>()
        for (line in nlbwPart.lines()) {
            val parts = line.trim().split(",")
            if (parts.size >= 4) {
                val mac = parts[0].replace('"', '').lowercase()
                val rx = parts[2].replace('"', '').toLongOrNull() ?: 0L
                val tx = parts[3].replace('"', '').toLongOrNull() ?: 0L
                nlbwRx[mac] = rx
                nlbwTx[mac] = tx
            }
        }"""

new_nlbw = """        val nlbwRx = mutableMapOf<String, Long>()
        val nlbwTx = mutableMapOf<String, Long>()
        var rxIdx = -1
        var txIdx = -1
        var macIdx = 0
        for ((idx, line) in nlbwPart.lines().withIndex()) {
            val parts = line.trim().split(",")
            if (idx == 0 && line.contains("rx_bytes", ignoreCase = true) || line.contains("rx", ignoreCase = true)) {
                for (i in parts.indices) {
                    val p = parts[i].replace("\\"", "").lowercase()
                    if (p.contains("rx")) rxIdx = i
                    if (p.contains("tx")) txIdx = i
                    if (p.contains("mac")) macIdx = i
                }
                continue
            }
            if (rxIdx == -1) {
                // Default fallback if no header matched
                if (parts.size >= 6) {
                    macIdx = 0
                    rxIdx = 3
                    txIdx = 4
                } else if (parts.size >= 4) {
                    macIdx = 0
                    rxIdx = 2
                    txIdx = 3
                }
            }
            
            if (parts.size > maxOf(rxIdx, txIdx, macIdx) && rxIdx != -1) {
                val mac = parts[macIdx].replace("\\"", "").lowercase()
                val rx = parts[rxIdx].replace("\\"", "").toLongOrNull() ?: 0L
                val tx = parts[txIdx].replace("\\"", "").toLongOrNull() ?: 0L
                nlbwRx[mac] = rx
                nlbwTx[mac] = tx
            }
        }"""

if old_nlbw in content:
    content = content.replace(old_nlbw, new_nlbw)
    with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
        f.write(content)
    print("nlbw parsing updated")
else:
    print("nlbw parsing not found")
