package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class TransactionService {
    private final Scanner scanner = new Scanner(System.in);

    // Issue a book with validation
    public void issueBook() {
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate if the book exists
        if (!isBookAvailable(bookId)) {
            System.out.println("❌ Error: Book does not exist in the library.");
            return;
        }

        // Check if the book is already issued
        if (isBookAlreadyIssued(bookId)) {
            System.out.println("❌ Error: This book is already issued to another user.");
            return;
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(15);

        String query = "INSERT INTO borrowed_books (user_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setDate(3, java.sql.Date.valueOf(issueDate));
            ps.setDate(4, java.sql.Date.valueOf(dueDate));
            ps.executeUpdate();
            System.out.println("✅ Book issued successfully! Due date: " + dueDate);
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to issue book.");
            e.printStackTrace();
        }
    }

    // Return a book and calculate fine if overdue
    public void returnBook() {
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();

        LocalDate today = LocalDate.now();
        LocalDate dueDate = null;
        
        String fetchQuery = "SELECT due_date FROM borrowed_books WHERE user_id = ? AND book_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement fetchStmt = con.prepareStatement(fetchQuery)) {
            fetchStmt.setInt(1, userId);
            fetchStmt.setInt(2, bookId);
            ResultSet rs = fetchStmt.executeQuery();
            if (rs.next()) {
                dueDate = rs.getDate("due_date").toLocalDate();
            } else {
                System.out.println("❌ No active borrow record found!");
                return;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to fetch due date.");
            e.printStackTrace();
            return;
        }

        long overdueDays = ChronoUnit.DAYS.between(dueDate, today);
        double fine = (overdueDays > 0) ? overdueDays * 5 : 0;

        System.out.println("✅ Book returned successfully! Fine amount: " + fine);

        if (fine > 0) {
            payFine(userId, bookId, fine);
        } else {
            removeBorrowRecord(userId, bookId);
        }
    }

    // Pay fine and remove borrow record
    public void payFine(int userId, int bookId, double fineAmount) {
        System.out.println("Fine amount due: " + fineAmount);
        System.out.print("Enter amount to pay: ");
        double paidAmount = scanner.nextDouble();

        if (paidAmount < fineAmount) {
            System.out.println("❌ Insufficient amount. Please pay the full fine.");
            return;
        }

        System.out.println("✅ Fine paid successfully! Returning book...");
        removeBorrowRecord(userId, bookId);
    }

    // Remove borrow record after returning a book
    private void removeBorrowRecord(int userId, int bookId) {
        String deleteQuery = "DELETE FROM borrowed_books WHERE user_id = ? AND book_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(deleteQuery)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            System.out.println("✅ Book successfully returned and removed from borrow records.");
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to return book.");
            e.printStackTrace();
        }
    }

    // Check if a book exists in the library
    private boolean isBookAvailable(int bookId) {
        String query = "SELECT book_id FROM books WHERE book_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to check book existence.");
            e.printStackTrace();
        }
        return false;
    }

    // Check if the book is already issued
    private boolean isBookAlreadyIssued(int bookId) {
        String query = "SELECT book_id FROM borrowed_books WHERE book_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to check book issuance status.");
            e.printStackTrace();
        }
        return false;
    }
}
