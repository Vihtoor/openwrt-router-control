import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Field;
public class test3 {
    public static void main(String[] args) throws Exception {
        File file = new File("/opt/gradle/.gradle/caches/modules-2/files-2.1/com.github.mwiede/jsch/0.2.20/2f2bff2fa80e34a2ffe03d2161bf3cb1f934f152/jsch-0.2.20.jar");
        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        Class<?> keyPairClass = loader.loadClass("com.jcraft.jsch.KeyPair");
        Class<?> jschClass = loader.loadClass("com.jcraft.jsch.JSch");
        Object jsch = jschClass.getDeclaredConstructor().newInstance();
        Object kp = keyPairClass.getMethod("genKeyPair", jschClass, int.class, int.class)
                .invoke(null, jsch, 5, 256); // 5 is KeyPair.ED25519
        
        Field vendorField = keyPairClass.getDeclaredField("vendor");
        vendorField.setAccessible(true);
        vendorField.setInt(kp, 4); // 4 is VENDOR_OPENSSH_V1

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        try {
            keyPairClass.getMethod("writePrivateKey", java.io.OutputStream.class).invoke(kp, out);
            System.out.println("Success!");
            System.out.println(out.toString());
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() != null) e.getCause().printStackTrace();
        }
    }
}
