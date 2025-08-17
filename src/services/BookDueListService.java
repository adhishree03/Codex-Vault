package services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDueListService {
    public void getOverDueDateList(int adminId, String adminName) {
        String query = "SELECT * FROM book_dues WHERE due_date < CURDATE()";
        try (Connection con = ConnectionClass.getConnectionMethod();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("book_id") + ", Due Date: " + rs.getDate("due_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
