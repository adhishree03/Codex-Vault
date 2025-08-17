package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AuthorService {

    // Method to add a new author
    public void addAuthor(int adminId, String adminName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Author Name:");
        String authorName = scanner.nextLine();

        String query = "INSERT INTO authors (name) VALUES (?)"; 
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, authorName);
            ps.executeUpdate();
            System.out.println("✅ Author added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all authors
    public void viewAuthors() {
        String query = "SELECT author_id, name FROM authors"; 

        try (Connection con = ConnectionClass.getConnectionMethod();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n📚 List of Authors:");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s\n", rs.getInt("author_id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an author's name
    public void updateAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Author ID to update:");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter New Author Name:");
        String newAuthorName = scanner.nextLine();

        String query = "UPDATE authors SET name = ? WHERE author_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, newAuthorName);
            ps.setInt(2, authorId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Author updated successfully!");
            } else {
                System.out.println("⚠️ No author found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an author
    public void deleteAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Author ID to delete:");
        int authorId = scanner.nextInt();

        String query = "DELETE FROM authors WHERE author_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, authorId);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Author deleted successfully!");
            } else {
                System.out.println("⚠️ No author found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
