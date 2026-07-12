fun main() {
    val nlbwPart = """"MAC","IPv4","IPv6","Rx Bytes","Tx Bytes","Connections"
"aa:bb:cc","1.1.1.1","-","100","200","1""""
    val nlbwRx = mutableMapOf<String, Long>()
    val nlbwTx = mutableMapOf<String, Long>()
    var rxIdx = -1
    var txIdx = -1
    var macIdx = 0
    for ((idx, line) in nlbwPart.lines().withIndex()) {
        val parts = line.trim().split(",")
        if (idx == 0 && line.contains("rx_bytes", ignoreCase = true) || line.contains("rx", ignoreCase = true)) {
            for (i in parts.indices) {
                val p = parts[i].replace("\"", "").lowercase()
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
            val mac = parts[macIdx].replace("\"", "").lowercase()
            val rx = parts[rxIdx].replace("\"", "").toLongOrNull() ?: 0L
            val tx = parts[txIdx].replace("\"", "").toLongOrNull() ?: 0L
            nlbwRx[mac] = rx
            nlbwTx[mac] = tx
        }
    }
    println("rx: $nlbwRx")
    println("tx: $nlbwTx")
}
