package services;
import org.mindrot.jbcrypt.BCrypt;

public class TestBCrypt {
    public static void main(String[] args) {
        String rawPassword = "admin123"; // Change this to your desired password
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
        System.out.println("🔒 Hashed Password: " + hashedPassword);
    }
}
