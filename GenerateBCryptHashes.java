import org.mindrot.jbcrypt.BCrypt;

public class GenerateBCryptHashes {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Provide passwords as arguments");
            return;
        }
        for (String pwd : args) {
            String hash = BCrypt.hashpw(pwd, BCrypt.gensalt(10));
            System.out.println(hash);
        }
    }
}
