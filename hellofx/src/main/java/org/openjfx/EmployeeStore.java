package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;

public class EmployeeStore {
    private static ObservableList<Employee> employees = FXCollections.observableArrayList();

    static {
        // Khởi tạo database và load dữ liệu
        initializeDatabase();
        loadEmployeesFromDatabase();
    }

    /**
     * Khởi tạo database và tạo các bảng cần thiết
     */
    private static void initializeDatabase() {
        try {
            DatabaseUtil.createEmployeesTable();
            DatabaseUtil.createUsersTable();

            // Thêm dữ liệu mẫu nếu chưa có
            if (!hasEmployees()) {
                addSampleData();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo database: " + e.getMessage());
        }
    }

    /**
     * Load danh sách nhân viên từ database
     */
    private static void loadEmployeesFromDatabase() {
        try {
            employees.clear();
            String sql = "SELECT id, name, position, salary, email, phone, address, hire_date FROM employees";
            List<Object[]> results = DatabaseUtil.executeQuery(sql);

            for (Object[] row : results) {
                Employee emp = new Employee(
                        (String) row[1], // name
                        (String) row[2], // position
                        ((Number) row[3]).doubleValue(), // salary
                        (String) row[5], // phone
                        (String) row[4], // email
                        (String) row[6], // address
                        true // isDatabase
                );
                emp.setId(((Number) row[0]).intValue()); // id
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi load nhân viên từ database: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra xem đã có nhân viên trong database chưa
     */
    private static boolean hasEmployees() throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees";
        Object result = DatabaseUtil.executeQueryForSingleValue(sql);
        return result != null && ((Number) result).intValue() > 0;
    }

    /**
     * Thêm dữ liệu mẫu vào database
     */
    private static void addSampleData() {
        try {
            // Thêm nhân viên
            String[] names = { "Đặng Văn Dũng", "Lê Văn Thể", "Trần Thị Giang", "Võ Bình Yên" };
            String[] positions = { "Quản lý", "Phục vụ bàn", "Thu Ngân", "Bảo vệ" };
            double[] salaries = { 10000000, 5000000, 10000000, 5000000 };
            String[] phones = { "0957654763", "0945423984", "0912345678", "0987654321" };
            String[] emails = { "admin1@coffee.com", "vanthe14@coffee.com", "giangtt@coffee.com", "yenvb@coffee.com" };
            String[] usernames = { "admin1", "vanthe14", "giangtt", "yenvb" };
            String[] passwords = { "123456", "123456", "123456", "123456" };
            String[] roles = { "MANAGER", "STAFF", "MANAGER", "STAFF" };

            for (int i = 0; i < names.length; i++) {
                // Thêm nhân viên
                String empSql = "INSERT INTO employees (name, position, salary, email, phone, hire_date) VALUES (?, ?, ?, ?, ?, CURDATE())";
                DatabaseUtil.executeUpdate(empSql, names[i], positions[i], salaries[i], emails[i], phones[i]);

                // Lấy ID của nhân viên vừa thêm
                String getIdSql = "SELECT LAST_INSERT_ID()";
                Object empId = DatabaseUtil.executeQueryForSingleValue(getIdSql);

                // Thêm user account
                String userSql = "INSERT INTO users (username, password, role, employee_id) VALUES (?, ?, ?, ?)";
                DatabaseUtil.executeUpdate(userSql, usernames[i], passwords[i], roles[i], empId);
            }

            System.out.println("Đã thêm dữ liệu mẫu vào database");
        } catch (SQLException e) {
            System.err.println("Lỗi thêm dữ liệu mẫu: " + e.getMessage());
        }
    }

    public static ObservableList<Employee> getEmployees() {
        return employees;
    }

    /**
     * Thêm nhân viên mới vào database
     */
    public static boolean addEmployee(Employee employee) {
        try {
            String sql = "INSERT INTO employees (name, position, salary, email, phone, address, hire_date) VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
            int result = DatabaseUtil.executeUpdate(sql,
                    employee.getName(),
                    employee.getPosition(),
                    employee.getSalary(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getAddress());

            if (result > 0) {
                // Lấy ID của nhân viên vừa thêm
                String getIdSql = "SELECT LAST_INSERT_ID()";
                Object empId = DatabaseUtil.executeQueryForSingleValue(getIdSql);
                employee.setId(((Number) empId).intValue());

                // Reload danh sách từ database
                loadEmployeesFromDatabase();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm nhân viên: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật thông tin nhân viên trong database
     */
    public static boolean updateEmployee(Employee employee) {
        try {
            String sql = "UPDATE employees SET name=?, position=?, salary=?, email=?, phone=?, address=? WHERE id=?";
            int result = DatabaseUtil.executeUpdate(sql,
                    employee.getName(),
                    employee.getPosition(),
                    employee.getSalary(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getAddress(),
                    employee.getId());

            if (result > 0) {
                // Reload danh sách từ database
                loadEmployeesFromDatabase();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật nhân viên: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa nhân viên khỏi database
     */
    public static boolean deleteEmployee(int employeeId) {
        try {
            // Xóa user account trước
            String deleteUserSql = "DELETE FROM users WHERE employee_id = ?";
            DatabaseUtil.executeUpdate(deleteUserSql, employeeId);

            // Xóa nhân viên
            String deleteEmpSql = "DELETE FROM employees WHERE id = ?";
            int result = DatabaseUtil.executeUpdate(deleteEmpSql, employeeId);

            if (result > 0) {
                // Reload danh sách từ database
                loadEmployeesFromDatabase();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa nhân viên: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tìm kiếm nhân viên theo tên
     */
    public static ObservableList<Employee> searchEmployees(String searchTerm) {
        ObservableList<Employee> searchResults = FXCollections.observableArrayList();
        try {
            String sql = "SELECT id, name, position, salary, email, phone, address, hire_date FROM employees WHERE name LIKE ?";
            List<Object[]> results = DatabaseUtil.executeQuery(sql, "%" + searchTerm + "%");

            for (Object[] row : results) {
                Employee emp = new Employee(
                        (String) row[1], // name
                        (String) row[2], // position
                        ((Number) row[3]).doubleValue(), // salary
                        (String) row[5], // phone
                        (String) row[4], // email
                        (String) row[6], // address
                        true // isDatabase
                );
                emp.setId(((Number) row[0]).intValue()); // id
                searchResults.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm nhân viên: " + e.getMessage());
        }
        return searchResults;
    }

    /**
     * Xác thực đăng nhập
     */
    public static Employee authenticateUser(String username, String password) {
        try {
            // Debug: Kiểm tra dữ liệu trong database
            System.out.println("Đang tìm kiếm user: " + username);

            String sql = "SELECT e.id, e.name, e.position, e.salary, e.email, e.phone, e.address, u.role " +
                    "FROM employees e " +
                    "JOIN users u ON e.id = u.employee_id " +
                    "WHERE u.username = ? AND u.password = ?";
            List<Object[]> results = DatabaseUtil.executeQuery(sql, username, password);

            System.out.println("Số kết quả tìm được: " + results.size());

            if (!results.isEmpty()) {
                Object[] row = results.get(0);
                System.out.println("Tìm thấy user: " + row[1] + " với role: " + row[7]);
                Employee emp = new Employee(
                        (String) row[1], // name
                        (String) row[2], // position
                        ((Number) row[3]).doubleValue(), // salary
                        (String) row[5], // phone
                        (String) row[4], // email
                        (String) row[6], // address
                        true // isDatabase
                );
                emp.setId(((Number) row[0]).intValue()); // id
                emp.setRole((String) row[7]); // role
                return emp;
            } else {
                // Debug: Kiểm tra xem có user nào trong database không
                String checkSql = "SELECT COUNT(*) FROM users";
                Object userCount = DatabaseUtil.executeQueryForSingleValue(checkSql);
                System.out.println("Tổng số user trong database: " + userCount);

                // Kiểm tra username có tồn tại không
                String userSql = "SELECT username FROM users";
                List<Object[]> allUsers = DatabaseUtil.executeQuery(userSql);
                System.out.println("Danh sách username trong database:");
                for (Object[] user : allUsers) {
                    System.out.println("- " + user[0]);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi xác thực đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
