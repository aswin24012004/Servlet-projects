package demo.learn.project.utils;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/bookstore?useSSL=false&serverTimezone=UTC";
    private static String uname = "root";
    private static String pass = "root";

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, uname, pass);
        } catch (Exception e) {
            System.err.println("DB Connection Error: " + e.getMessage());
        }
        return con;
    }
}