fun main() {
    val chunk = "\u001b]0;root@OpenWrt:/etc\u0007root@OpenWrt:/etc# "
    val regex = Regex("\\x1b\\]0;.*?\\x07")
    println(chunk.replace(regex, ""))
}
