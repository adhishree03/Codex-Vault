package services;

import java.util.Scanner;

public class OtherServices {
    private static final Scanner scanner = new Scanner(System.in);
    
    public void indexMethod() {
        while (true) {
            System.out.println("\n? Welcome to the Library Management System ?");
            System.out.println("1️⃣ Admin Login");
            System.out.println("2️⃣ User Login");
            System.out.println("3️⃣ User Registration");
            System.out.println("4️⃣ Exit");
            System.out.print("Enter your choice: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input! Please enter a number.");
                scanner.next(); // Clear buffer
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1 -> new AdminService().adminLogin();
                case 2 -> new UserService().userLogin();
                case 3 -> new UserService().registerUser();
                case 4 -> {
                    System.out.println("👋 Exiting... Thank you for using the system!");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid choice! Please enter a number between 1 and 4.");
            }
        }
    }
}
