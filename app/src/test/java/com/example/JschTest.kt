package com.example
import org.junit.Test
import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.io.ByteArrayOutputStream
class JschTest {
    @Test
    fun testGenEd25519() {
        Security.addProvider(BouncyCastleProvider())
        val jsch = JSch()
        val kp = KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256)
        val out = ByteArrayOutputStream()
        kp.writePrivateKey(out, null)
        println("Success ED25519 write!")
    }
}
