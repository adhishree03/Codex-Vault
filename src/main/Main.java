package main;

import java.util.Scanner;
import services.AdminService;
import services.MaintenanceService;
import services.TransactionService;
import services.UserService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        AdminService adminService = new AdminService();
        MaintenanceService maintenanceService = new MaintenanceService();
        TransactionService transactionService = new TransactionService();

        while (true) {
            System.out.println("\n📚 Welcome to the Library Management System 📚");
            System.out.println("1️⃣ Admin Login");
            System.out.println("2️⃣ User Login");
            System.out.println("3️⃣ User Registration"); 
            System.out.println("4️⃣ Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (adminService.adminLogin()) {
                        adminMenu(scanner, maintenanceService, transactionService);
                    }
                    break;
                case 2:
                    if (userService.userLogin()) {
                        userMenu(scanner, transactionService);
                    }
                    break;
                case 3:
                    userService.registerUser(); 
                    break;
                case 4:
                    System.out.println("👋 Exiting the system...");
                    return;
                default:
                    System.out.println("❌ Invalid choice! Please enter a valid option.");
            }
        }
    }

    private static void adminMenu(Scanner scanner, MaintenanceService maintenanceService, TransactionService transactionService) {
        while (true) {
            System.out.println("\n🔹 Admin Panel:");
            System.out.println("1️⃣ View Reports");
            System.out.println("2️⃣ Manage Transactions");
            System.out.println("3️⃣ Generate Maintenance Reports");
            System.out.println("4️⃣ Logout");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    maintenanceService.generateReports();
                    break;
                case 2:
                    transactionMenu(scanner, transactionService);
                    break;
                case 3:
                    System.out.println("📊 Generating maintenance reports...");
                    maintenanceService.generateReports();
                    break;
                case 4:
                    System.out.println("🔴 Admin logged out.");
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private static void userMenu(Scanner scanner, TransactionService transactionService) {
        UserService userService = new UserService(); // Initialize UserService
        userService.userHome(); // ✅ Redirects to userHome()
    }

    private static void transactionMenu(Scanner scanner, TransactionService transactionService) {
        while (true) {
            System.out.println("\n🔹 Transaction Menu:");
            System.out.println("1️⃣ Issue Book");
            System.out.println("2️⃣ Return Book");
            System.out.println("3️⃣ Pay Fine");
            System.out.println("4️⃣ Back to Main Menu");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    transactionService.issueBook();
                    break;
                case 2:
                    transactionService.returnBook();
                    break;
                case 3:
                    System.out.print("Enter User ID: ");
                    int userId = scanner.nextInt();
                    
                    System.out.print("Enter Book ID: ");
                    int bookId = scanner.nextInt();
                    
                    System.out.print("Enter Fine Amount: ");
                    double fineAmount = scanner.nextDouble();
                
                    transactionService.payFine(userId, bookId, fineAmount);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
