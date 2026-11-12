import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair
fun main() {
    val jsch = JSch()
    try {
        val keyPair = KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256)
        println("ED25519 Gen OK")
    } catch(e: Exception) {
        println("ED25519 Gen Error: ${e.message}")
        e.printStackTrace()
    }
}
