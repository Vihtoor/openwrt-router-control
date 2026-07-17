fun main() {
    val currentTypedLine = ""
    val before = 1
    val textToDelete = currentTypedLine.takeLast(before)
    val bytesToDelete = textToDelete.toByteArray(Charsets.UTF_8).size
    println("bytesToDelete: $bytesToDelete")
}
