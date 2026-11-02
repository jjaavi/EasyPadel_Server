import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneradorHashes {

    private static final String[] PASSWORDS_PER_DEFECTE = {
            "12345678."
    };

    public static void main(String[] args) {
        String[] passwords = args.length > 0 ? args : PASSWORDS_PER_DEFECTE;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (String password : passwords) {
            System.out.println(password + " -> " + encoder.encode(password));
        }
    }
}
