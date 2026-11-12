import org.bouncycastle.crypto.util.OpenSSHPrivateKeyUtil;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import java.security.SecureRandom;
import java.util.Base64;
import com.jcraft.jsch.JSch;
public class test8 {
    public static void main(String[] args) throws Exception {
        Ed25519KeyPairGenerator gen = new Ed25519KeyPairGenerator();
        gen.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair kp = gen.generateKeyPair();
        byte[] encoded = OpenSSHPrivateKeyUtil.encodePrivateKey(kp.getPrivate());
        String pem = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
                     Base64.getEncoder().encodeToString(encoded) + "\n" +
                     "-----END OPENSSH PRIVATE KEY-----\n";
        JSch jsch = new JSch();
        com.jcraft.jsch.KeyPair jschKp = com.jcraft.jsch.KeyPair.load(jsch, pem.getBytes(), null);
        System.out.println("Loaded JSch Key Type: " + jschKp.getKeyTypeString());
    }
}
