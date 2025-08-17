package services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "anushree";

    public static Connection getConnectionMethod() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver Not Found! Add the JAR file.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed! Check database name, username, and password.");
            e.printStackTrace();
        }
        return con;
    }
}
