package org.openjfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class quản lý cấu hình và kết nối cơ sở dữ liệu MySQL
 */
public class DatabaseConfig {

    // Thông tin kết nối cơ sở dữ liệu
    private static final String DB_URL = "jdbc:mysql://localhost:3306/coffee_shop_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "12345"; // Thay đổi mật khẩu theo cài đặt của bạn
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    /**
     * Lấy kết nối đến cơ sở dữ liệu
     * 
     * @return Connection object
     * @throws SQLException nếu không thể kết nối
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Đăng ký driver MySQL
                Class.forName(DB_DRIVER);

                // Tạo kết nối mới
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("Kết nối MySQL thành công!");
            } catch (ClassNotFoundException e) {
                System.err.println("Không tìm thấy MySQL Driver: " + e.getMessage());
                throw new SQLException("MySQL Driver không được tìm thấy", e);
            } catch (SQLException e) {
                System.err.println("Lỗi kết nối MySQL: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Đóng kết nối cơ sở dữ liệu
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đã đóng kết nối MySQL");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    /**
     * Kiểm tra kết nối có hoạt động không
     * 
     * @return true nếu kết nối thành công, false nếu không
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra kết nối: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy URL cơ sở dữ liệu
     * 
     * @return URL của cơ sở dữ liệu
     */
    public static String getDbUrl() {
        return DB_URL;
    }

    /**
     * Lấy tên người dùng cơ sở dữ liệu
     * 
     * @return username
     */
    public static String getDbUsername() {
        return DB_USERNAME;
    }
}
