package org.openjfx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class tiện ích để thực hiện các thao tác CRUD với cơ sở dữ liệu MySQL
 */
public class DatabaseUtil {

    /**
     * Thực hiện câu lệnh SQL không trả về kết quả (INSERT, UPDATE, DELETE)
     * 
     * @param sql    câu lệnh SQL
     * @param params các tham số cho câu lệnh SQL
     * @return số dòng bị ảnh hưởng
     * @throws SQLException nếu có lỗi SQL
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);

            // Thiết lập các tham số
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } finally {
            closeResources(stmt, null);
        }
    }

    /**
     * Thực hiện câu lệnh SELECT và trả về danh sách kết quả
     * 
     * @param sql    câu lệnh SQL
     * @param params các tham số cho câu lệnh SQL
     * @return danh sách kết quả
     * @throws SQLException nếu có lỗi SQL
     */
    public static List<Object[]> executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object[]> results = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);

            // Thiết lập các tham số
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                results.add(row);
            }

            return results;
        } finally {
            closeResources(stmt, rs);
        }
    }

    /**
     * Thực hiện câu lệnh SELECT và trả về một giá trị duy nhất
     * 
     * @param sql    câu lệnh SQL
     * @param params các tham số cho câu lệnh SQL
     * @return giá trị đầu tiên của cột đầu tiên
     * @throws SQLException nếu có lỗi SQL
     */
    public static Object executeQueryForSingleValue(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);

            // Thiết lập các tham số
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } finally {
            closeResources(stmt, rs);
        }
    }

    /**
     * Kiểm tra xem một bản ghi có tồn tại không
     * 
     * @param sql    câu lệnh SQL SELECT với COUNT(*)
     * @param params các tham số cho câu lệnh SQL
     * @return true nếu tồn tại, false nếu không
     * @throws SQLException nếu có lỗi SQL
     */
    public static boolean recordExists(String sql, Object... params) throws SQLException {
        Object result = executeQueryForSingleValue(sql, params);
        if (result instanceof Number) {
            return ((Number) result).intValue() > 0;
        }
        return false;
    }

    /**
     * Đóng các tài nguyên database
     * 
     * @param stmt PreparedStatement
     * @param rs   ResultSet
     */
    private static void closeResources(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    /**
     * Tạo bảng employees nếu chưa tồn tại
     * 
     * @throws SQLException nếu có lỗi SQL
     */
    public static void createEmployeesTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "position VARCHAR(50) NOT NULL," +
                "salary DECIMAL(10,2) NOT NULL," +
                "email VARCHAR(100) UNIQUE," +
                "phone VARCHAR(20)," +
                "address TEXT," +
                "hire_date DATE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";

        executeUpdate(sql);
        System.out.println("Bảng employees đã được tạo hoặc đã tồn tại");
    }

    /**
     * Tạo bảng users nếu chưa tồn tại
     * 
     * @throws SQLException nếu có lỗi SQL
     */
    public static void createUsersTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "role ENUM('MANAGER', 'STAFF') NOT NULL," +
                "employee_id INT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE SET NULL" +
                ")";

        executeUpdate(sql);
        System.out.println("Bảng users đã được tạo hoặc đã tồn tại");
    }
}
