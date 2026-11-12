import java.io.ByteArrayOutputStream;
import com.jcraft.jsch.*;
public class test9 {
    public static void main(String[] args) throws Exception {
        JSch jsch = new JSch();
        KeyPair kp = KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            kp.writePrivateKey(out, null);
            System.out.println("Success! " + out.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
