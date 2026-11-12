import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

fun main() {
    try {
        Security.removeProvider("BC")
        Security.insertProviderAt(BouncyCastleProvider(), 1)
        val jsch = JSch()
        val keyPair = KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256)
        println("Fingerprint: ${keyPair.fingerPrint}")
        println("Generated successfully!")
    } catch(e: Exception) {
        println("Exception class: ${e.javaClass.name}")
        println("Exception message: ${e.message}")
        e.printStackTrace()
    }
}
