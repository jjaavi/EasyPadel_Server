import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*Generador de hashes per a les contrasenyes de les dades de prova de /bd/dades_prova.sql
Aquest codi es independent al programa i es pot utilitzar de forma local per fer proves.
Es pot executar amb:
    javac -cp "lib\*" GeneradorHashes.java
    java -cp ".;lib\*" GeneradorHashes
 */
public class GeneradorHashes {

    private static final String[] PASSWORDS_PER_DEFECTE = {
            "MarcGarcia123",
            "LauraPons123",
            "PauSerra123",
            "AnnaVidal123",
            "JoanFerrer123"
    };

    public static void main(String[] args) {
        String[] passwords = args.length > 0 ? args : PASSWORDS_PER_DEFECTE;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (String password : passwords) {
            System.out.println(password + " -> " + encoder.encode(password));
        }
    }
}
