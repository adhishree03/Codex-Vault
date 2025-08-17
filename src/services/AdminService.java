package services;

import entities.Admin;
import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement; // ✅ Import BCrypt for password hashing
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class AdminService extends Admin {
    private static final Scanner scanner = new Scanner(System.in);

    public boolean adminLogin() {
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println("---------------------------");
        System.out.println("|       WELCOME TO        |");
        System.out.println("|    LIBRARY MANAGEMENT   |");
        System.out.println("|         SYSTEM          |");
        System.out.println("|      Admin  Login       |");
        System.out.println("---------------------------");
    
        while (true) {
            try (Connection con = ConnectionClass.getConnectionMethod();
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT user_id, name, password FROM users WHERE LOWER(name) = LOWER(?) AND is_admin = 1")) {

                System.out.print("Enter Username: ");
                String username = scanner.nextLine().trim();

                String password = getMaskedPassword();

                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    if (BCrypt.checkpw(password, hashedPassword)) { // Verify hashed password
                        int adminId = rs.getInt("user_id");
                        String adminName = rs.getString("name");
                        System.out.println("✅ Login Success.");
                        adminHome(adminId, adminName);
                        return true;
                    } else {
                        System.out.println("❌ Incorrect Password.");
                    }
                } else {
                    System.out.println("❌ Invalid Username.");
                }

                System.out.println("1️⃣ Try again | 2️⃣ Exit");

                if (!scanner.hasNextInt()) {
                    System.out.println("⚠️ Invalid input! Please enter a number.");
                    scanner.next(); // Clear invalid input
                    continue;
                }

                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (option == 2) {
                    new OtherServices().indexMethod();
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("❌ Database Error: " + e.getMessage());
                return false;
            }
        }
    }

    private String getMaskedPassword() {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword("Enter Password: ");
            return new String(passwordArray);
        } else {
            System.out.print("Enter Password: ");
            StringBuilder password = new StringBuilder();
            while (true) {
                char ch = readCharacter(); // Read character
                if (ch == '\n') break;    // Stop on Enter key
                if (ch == '\b' && password.length() > 0) {
                    password.deleteCharAt(password.length() - 1); // Handle backspace
                    System.out.print("\b \b");
                } else {
                    password.append(ch);
                    System.out.print("*"); // Show '*' instead of character
                }
            }
            return password.toString();
        }
    }

    private char readCharacter() {
        try {
            return (char) System.in.read();
        } catch (Exception e) {
            return '\n';
        }
    }

    public void adminHome(int adminId, String adminName) {
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println("---------------------------");
        System.out.println("|        Admin Dashboard       |");
        System.out.println("---------------------------");
        System.out.println("👋 Welcome, " + adminName);
        System.out.println("---------------------------");

        String userCountQuery = "SELECT COUNT(user_id) FROM users";
        String bookCountQuery = "SELECT COUNT(book_id) FROM books";
        String languageCountQuery = "SELECT COUNT(language_id) FROM languages";
        String authorCountQuery = "SELECT COUNT(author_id) FROM authors";

        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement psUser = con.prepareStatement(userCountQuery);
             PreparedStatement psBook = con.prepareStatement(bookCountQuery);
             PreparedStatement psLanguage = con.prepareStatement(languageCountQuery);
             PreparedStatement psAuthor = con.prepareStatement(authorCountQuery)) {

            ResultSet rsUser = psUser.executeQuery();
            ResultSet rsBook = psBook.executeQuery();
            ResultSet rsLanguage = psLanguage.executeQuery();
            ResultSet rsAuthor = psAuthor.executeQuery();

            rsUser.next();
            rsBook.next();
            rsLanguage.next();
            rsAuthor.next();

            System.out.printf("📌 Total Users    : %d%n", rsUser.getInt(1));
            System.out.printf("📌 Total Books    : %d%n", rsBook.getInt(1));
            System.out.printf("📌 Total Languages: %d%n", rsLanguage.getInt(1));
            System.out.printf("📌 Total Authors  : %d%n", rsAuthor.getInt(1));
            System.out.println("---------------------------");

            while (true) {
                System.out.println("| 1️⃣  Add Books          |");
                System.out.println("| 2️⃣  Add Authors        |");
                System.out.println("| 3️⃣  Add Languages      |");
                System.out.println("| 4️⃣  View Books         |");
                System.out.println("| 5️⃣  View Authors       |");
                System.out.println("| 6️⃣  View Languages     |");
                System.out.println("| 7️⃣  View Due Date List |");
                System.out.println("| 8️⃣  Generate Reports   |");
                System.out.println("| 9️⃣  Manage Transactions|");
                System.out.println("| 🔟  Logout             |");
                System.out.println("---------------------------");
                System.out.print("Enter an Option: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("⚠️ Invalid input! Please enter a number.");
                    scanner.next(); // Clear buffer
                    continue;
                }

                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1 -> new BookService().addBook();
                    case 2 -> new AuthorService().addAuthor(adminId, adminName);
                    case 3 -> new LanguageService().addLanguage(adminId, adminName);
                    case 4 -> new BookService().adminViewBooks(adminId, adminName);
                    case 5 -> new AuthorService().viewAuthors();
                    case 6 -> new LanguageService().viewLanguages();
                    case 7 -> new BookDueListService().getOverDueDateList(adminId, adminName);
                    case 8 -> {
                        System.out.println("📊 Generating Reports...");
                        new MaintenanceService().generateReports();
                    }
                    case 9 -> transactionMenu();
                    case 10 -> {
                        System.out.println("🔴 Logging out...");
                        new OtherServices().indexMethod();
                        return;
                    }
                    default -> System.out.println("❌ Invalid option! Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        }
    }

    private void transactionMenu() {
        while (true) {
            System.out.println("\n🔹 Transaction Menu:");
            System.out.println("| 1️⃣  Issue Book        |");
            System.out.println("| 2️⃣  Return Book       |");
            System.out.println("| 3️⃣  Pay Fine         |");
            System.out.println("| 4️⃣  Back to Dashboard |");
            System.out.println("---------------------------");
            System.out.print("Enter an Option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next(); // Clear buffer
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            TransactionService transactionService = new TransactionService();

            switch (choice) {
                case 1 -> transactionService.issueBook();
                case 2 -> transactionService.returnBook();
                case 3 -> {
                    System.out.print("Enter User ID: ");
                    int userId = scanner.nextInt();
                    
                    System.out.print("Enter Book ID: ");
                    int bookId = scanner.nextInt();
                    
                    System.out.print("Enter Fine Amount: ");
                    double fineAmount = scanner.nextDouble();
                
                    transactionService.payFine(userId, bookId, fineAmount);
                }
                case 4 -> {
                    System.out.println("🔙 Returning to Admin Dashboard...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}
