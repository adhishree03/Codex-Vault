package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;


public class FixPasswordHash {
    public static void main(String[] args) {
        try (Connection con = ConnectionClass.getConnectionMethod()) {
            String selectQuery = "SELECT user_id, password FROM users WHERE password NOT LIKE '$2a$%'";
            PreparedStatement psSelect = con.prepareStatement(selectQuery);
            ResultSet rs = psSelect.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String plainPassword = rs.getString("password");

                // Hash the password using BCrypt
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                // Update the password in the database
                String updateQuery = "UPDATE users SET password = ? WHERE user_id = ?";
                PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                psUpdate.setString(1, hashedPassword);
                psUpdate.setInt(2, userId);
                psUpdate.executeUpdate();

                System.out.println("✅ Fixed password for User ID: " + userId);
            }
            System.out.println("✅ All plain-text passwords have been hashed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
