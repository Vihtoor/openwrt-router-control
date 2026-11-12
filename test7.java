import org.bouncycastle.crypto.util.OpenSSHPrivateKeyUtil;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import java.security.SecureRandom;
import java.util.Base64;
public class test7 {
    public static void main(String[] args) throws Exception {
        Ed25519KeyPairGenerator gen = new Ed25519KeyPairGenerator();
        gen.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair kp = gen.generateKeyPair();
        byte[] encoded = OpenSSHPrivateKeyUtil.encodePrivateKey(kp.getPrivate());
        System.out.println("-----BEGIN OPENSSH PRIVATE KEY-----");
        System.out.println(Base64.getEncoder().encodeToString(encoded));
        System.out.println("-----END OPENSSH PRIVATE KEY-----");
    }
}
