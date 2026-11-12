import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class test_jsch_bc {
    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            JSch jsch = new JSch();
            KeyPair kp = KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256);
            System.out.println("Success! " + kp.getFingerPrint());
        } catch(Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }
}
