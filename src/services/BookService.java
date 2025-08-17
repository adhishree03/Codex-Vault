package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BookService {

    // Method to add a new book
    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Book Title:");
        String bookTitle = scanner.nextLine();
        System.out.println("Enter Author ID:");
        int authorId = scanner.nextInt();
        System.out.println("Enter Language ID:");
        int languageId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.println("Enter Genre:");
        String genre = scanner.nextLine();

        String query = "INSERT INTO books (title, author_id, language_id, genre) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, bookTitle);
            ps.setInt(2, authorId);
            ps.setInt(3, languageId);
            ps.setString(4, genre);
            ps.executeUpdate();
            System.out.println("✅ Book added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all books
    public void viewBooks() {
        String query = "SELECT b.book_id, b.title, a.name AS author, l.name AS language, b.genre, b.rating, b.available " +
                       "FROM books b " +
                       "JOIN authors a ON b.author_id = a.author_id " +
                       "JOIN languages l ON b.language_id = l.language_id";

        try (Connection con = ConnectionClass.getConnectionMethod();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n📚 List of Books:");
            while (rs.next()) {
                System.out.printf("ID: %d | Title: %s | Author: %s | Language: %s | Genre: %s | Rating: %.1f | Available: %s\n",
                        rs.getInt("book_id"), rs.getString("title"), rs.getString("author"),
                        rs.getString("language"), rs.getString("genre"), rs.getFloat("rating"),
                        rs.getBoolean("available") ? "✅ Yes" : "❌ No");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update book details
    public void updateBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Book ID to update:");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.println("Enter new Title:");
        String newTitle = scanner.nextLine();
        System.out.println("Enter new Genre:");
        String newGenre = scanner.nextLine();

        String query = "UPDATE books SET title = ?, genre = ? WHERE book_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, newTitle);
            ps.setString(2, newGenre);
            ps.setInt(3, bookId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Book updated successfully!");
            } else {
                System.out.println("⚠️ No book found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a book
    public void deleteBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Book ID to delete:");
        int bookId = scanner.nextInt();

        String query = "DELETE FROM books WHERE book_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, bookId);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Book deleted successfully!");
            } else {
                System.out.println("⚠️ No book found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adminViewBooks(int adminId, String adminName) {
        System.out.println("\n🔹 Admin: " + adminName + " (ID: " + adminId + ") is viewing all books.");
    
        String query = "SELECT b.book_id, b.title, a.name AS author, l.name AS language, b.genre, b.rating, b.available " +
                       "FROM books b " +
                       "JOIN authors a ON b.author_id = a.author_id " +
                       "JOIN languages l ON b.language_id = l.language_id";
    
        try (Connection con = ConnectionClass.getConnectionMethod();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\n📚 List of Books (Admin View):");
            while (rs.next()) {
                System.out.printf("ID: %d | Title: %s | Author: %s | Language: %s | Genre: %s | Rating: %.1f | Available: %s\n",
                        rs.getInt("book_id"), rs.getString("title"), rs.getString("author"),
                        rs.getString("language"), rs.getString("genre"), rs.getFloat("rating"),
                        rs.getBoolean("available") ? "✅ Yes" : "❌ No");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
