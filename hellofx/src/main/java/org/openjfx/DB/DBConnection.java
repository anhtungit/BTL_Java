package org.openjfx.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection dbConnection;
    private static Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/coffee_shop";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345";

    private DBConnection() {
        connect();
    }

    private static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối MySQL: " + e.getMessage());
            e.printStackTrace();
            connection = null;
        }
    }

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public static Connection getConnection() {
        try {
            // Kiểm tra connection có null hoặc đã đóng không
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠️ Connection bị mất, đang kết nối lại...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi kiểm tra connection: " + e.getMessage());
            connect();
        }
        return connection;
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

}
