import com.jcraft.jsch.JSch;
public class test4 {
    public static void main(String[] args) throws Exception {
        System.out.println(JSch.getConfig("keypairgen.eddsa"));
    }
}
