import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

fun main() {
    println(BouncyCastleProvider().name)
}
