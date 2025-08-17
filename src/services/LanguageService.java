package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LanguageService {

    // Method to add a new language
    public void addLanguage(int adminId, String adminName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Language Name:");
        String languageName = scanner.nextLine();

        String query = "INSERT INTO languages (name) VALUES (?)"; 
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, languageName);
            ps.executeUpdate();
            System.out.println("✅ Language added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all available languages
    public void viewLanguages() {
        String query = "SELECT language_id, name FROM languages";

        try (Connection con = ConnectionClass.getConnectionMethod();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n🌍 Available Languages:");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s\n", rs.getInt("language_id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a language name
    public void updateLanguage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Language ID to update:");
        int languageId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.println("Enter New Language Name:");
        String newLanguageName = scanner.nextLine();

        String query = "UPDATE languages SET name = ? WHERE language_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, newLanguageName);
            ps.setInt(2, languageId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Language updated successfully!");
            } else {
                System.out.println("⚠️ No language found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a language
    public void deleteLanguage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Language ID to delete:");
        int languageId = scanner.nextInt();

        String query = "DELETE FROM languages WHERE language_id = ?";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, languageId);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Language deleted successfully!");
            } else {
                System.out.println("⚠️ No language found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
