import com.jcraft.jsch.*;
public class test {
    public static void main(String[] args) throws Exception {
        JSch jsch = new JSch();
        try {
            KeyPair.genKeyPair(jsch, KeyPair.ED25519, 256);
            System.out.println("OK");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
