import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

fun main() {
    Security.addProvider(BouncyCastleProvider())
    val jsch = JSch()
    try {
        val kp = KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256)
        println("Success ED25519")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
