package services;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final Scanner scanner = new Scanner(System.in);
    private int loggedInUserId = -1; // ✅ Track the logged-in user ID

    public void userPanel() {
        while (true) {
            System.out.println("________________________________________________________________________________________________________________");
            System.out.println("---------------------------");
            System.out.println("|       WELCOME TO        |");
            System.out.println("|    LIBRARY MANAGEMENT   |");
            System.out.println("|         SYSTEM          |");
            System.out.println("|      User Login         |");
            System.out.println("---------------------------");
            System.out.println("1️⃣ Register");
            System.out.println("2️⃣ Login");
            System.out.println("3️⃣ Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> {
                    if (userLogin()) {
                        userHome();
                    }
                }
                case 3 -> {
                    System.out.println("🔴 Exiting...");
                    return;
                }
                default -> System.out.println("❌ Invalid option! Try again.");
            }
        }
    }

    public void registerUser() {
        System.out.print("Enter User Name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        String password = maskPassword();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userName);
            ps.setString(2, email.trim().toLowerCase());
            ps.setString(3, hashedPassword);
            ps.executeUpdate();
            System.out.println("✅ User registered successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to register user.");
        }
    }

    public boolean userLogin() {
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println("---------------------------");
        System.out.println("|       WELCOME TO        |");
        System.out.println("|    LIBRARY MANAGEMENT   |");
        System.out.println("|         SYSTEM          |");
        System.out.println("|      User Login         |");
        System.out.println("---------------------------");

        System.out.print("Enter User Email: ");
        String email = scanner.nextLine().trim();
        String password = maskPassword();

        int userId = authenticateUser(email, password);
        if (userId != -1) {
            loggedInUserId = userId;
            userHome();
            return true;
        }
        return false;
    }

    public int authenticateUser(String email, String password) {
        String query = "SELECT user_id, password FROM users WHERE email = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String storedHashedPassword = rs.getString("password");

                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        loggedInUserId = userId;
                        System.out.println("✅ Login successful! Welcome, " + email);
                        return userId;
                    } else {
                        System.out.println("❌ Invalid Credentials.");
                    }
                } else {
                    System.out.println("❌ No user found with this email.");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: Database connection issue.");
        }
        return -1;
    }

    public void userHome() {
        while (true) {
            System.out.println("________________________________________________________________________________________________________________");
            System.out.println("---------------------------");
            System.out.println("|        User Panel       |");
            System.out.println("---------------------------");
            System.out.println("1️⃣ View Reports");
            System.out.println("2️⃣ Issue Book");
            System.out.println("3️⃣ Return Book");
            System.out.println("4️⃣ Update Profile");
            System.out.println("5️⃣ Delete Account");
            System.out.println("6️⃣ Logout");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> System.out.println("📊 Viewing Reports...");
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 6 -> {
                    System.out.println("🔴 Logging out...");
                    loggedInUserId = -1;
                    return;
                }
                default -> System.out.println("❌ Invalid option! Try again.");
            }
        }
    }

    public void issueBook() {
        if (loggedInUserId == -1) {
            System.out.println("⚠️ Please log in to issue a book.");
            return;
        }
    
        System.out.print("Enter Book ID to Issue: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
    
        String checkQuery = "SELECT * FROM books WHERE book_id = ? AND is_issued = false";
        String issueQuery = "UPDATE books SET is_issued = true, issued_to = ? WHERE book_id = ?";
    
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            
            checkStmt.setInt(1, bookId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ Book is not available for issue.");
                    return;
                }
            }
    
            try (PreparedStatement issueStmt = con.prepareStatement(issueQuery)) {
                issueStmt.setInt(1, loggedInUserId);
                issueStmt.setInt(2, bookId);
                issueStmt.executeUpdate();
                System.out.println("✅ Book Issued Successfully!");
            }
    
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to issue book.");
            e.printStackTrace();
        }
    }
    
    

    public void returnBook() {
        if (loggedInUserId == -1) {
            System.out.println("⚠️ Please log in to return a book.");
            return;
        }
    
        System.out.print("Enter Book ID to Return: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
    
        String checkQuery = "SELECT * FROM books WHERE book_id = ? AND issued_to = ?";
        String returnQuery = "UPDATE books SET is_issued = false, issued_to = NULL WHERE book_id = ?";
    
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            
            checkStmt.setInt(1, bookId);
            checkStmt.setInt(2, loggedInUserId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ You cannot return this book (not issued to you).");
                    return;
                }
            }
    
            try (PreparedStatement returnStmt = con.prepareStatement(returnQuery)) {
                returnStmt.setInt(1, bookId);
                returnStmt.executeUpdate();
                System.out.println("✅ Book Returned Successfully!");
            }
    
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to return book.");
            e.printStackTrace();
        }
    }
    
    
    private String maskPassword() {
        Console console = System.console();
        if (console != null) {
            return new String(console.readPassword("Enter Password: "));
        }
        System.out.print("Enter Password: ");
        return scanner.nextLine();
    }

    public void updateUser() {
        System.out.print("Enter new Name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new Email: ");
        String newEmail = scanner.nextLine();

        String query = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, newName);
            ps.setString(2, newEmail.toLowerCase());
            ps.setInt(3, loggedInUserId);

            ps.executeUpdate();
            System.out.println("✅ Profile updated successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to update profile.");
        }
    }
    public void deleteUser() {
        if (loggedInUserId == -1) {
            System.out.println("⚠️ You must be logged in to delete your account.");
            return;
        }
    
        System.out.print("❗ Are you sure you want to delete your account? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
    
        if (!confirm.equals("yes")) {
            System.out.println("❌ Account deletion cancelled.");
            return;
        }
    
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
    
            ps.setInt(1, loggedInUserId);
    
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Account deleted successfully!");
                loggedInUserId = -1;
            } else {
                System.out.println("⚠️ No user found.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to delete user.");
        }
    }
    
}
