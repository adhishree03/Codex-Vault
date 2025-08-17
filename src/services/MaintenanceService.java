package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaintenanceService {
    public void generateReports() {
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM borrowed_books")) {
            ResultSet rs = ps.executeQuery();
            System.out.println("\n📊 Transaction Reports:");
            while (rs.next()) {
                System.out.printf("ID: %d | User ID: %d | Book ID: %d | Issue Date: %s | Due Date: %s\n",
                        rs.getInt("borrow_id"), rs.getInt("user_id"),
                        rs.getInt("book_id"), rs.getDate("borrow_date"), rs.getDate("due_date"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: Unable to generate reports.");
            e.printStackTrace();
        }
    }
}