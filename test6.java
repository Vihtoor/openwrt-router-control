import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.jcraft.jsch.*;
import java.security.*;
import java.io.*;
public class test6 {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519", "BC");
        java.security.KeyPair kp = kpg.generateKeyPair();
        StringWriter sw = new StringWriter();
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(sw)) {
            pemWriter.writeObject(kp.getPrivate());
        }
        String pem = sw.toString();
        System.out.println("Generated PEM.");
        JSch jsch = new JSch();
        try {
            com.jcraft.jsch.KeyPair jschKp = com.jcraft.jsch.KeyPair.load(jsch, pem.getBytes(), null);
            System.out.println("Loaded with JSch! Type: " + jschKp.getKeyType());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
