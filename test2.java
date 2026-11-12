import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;
public class test2 {
    public static void main(String[] args) throws Exception {
        File file = new File("/opt/gradle/.gradle/caches/modules-2/files-2.1/com.github.mwiede/jsch/0.2.20/2f2bff2fa80e34a2ffe03d2161bf3cb1f934f152/jsch-0.2.20.jar");
        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        Class<?> keyPairClass = loader.loadClass("com.jcraft.jsch.KeyPair");
        Class<?> jschClass = loader.loadClass("com.jcraft.jsch.JSch");
        Object jsch = jschClass.getDeclaredConstructor().newInstance();
        System.out.println("Trying ED25519");
        try {
            Object kp = keyPairClass.getMethod("genKeyPair", jschClass, int.class, int.class)
                .invoke(null, jsch, 4, 256); // 4 is KeyPair.ED25519
            System.out.println("OK!");
        } catch(Exception e) {
            e.printStackTrace();
            if(e.getCause() != null) e.getCause().printStackTrace();
        }
    }
}
