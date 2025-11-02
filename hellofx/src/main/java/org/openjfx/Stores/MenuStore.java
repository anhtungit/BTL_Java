package org.openjfx.Stores;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.DB.DBConnection;
import org.openjfx.Models.MenuItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MenuStore {
    private static final ObservableList<MenuItem> items = FXCollections.observableArrayList();
    private static boolean hasInitialized = false;

    // Helper: Kiểm tra và lấy connection
    private static Connection getConnection() {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            throw new RuntimeException("Không thể kết nối với database. Vui lòng kiểm tra MySQL đã chạy chưa!");
        }
        return conn;
    }

    public static void loadFromDatabase() {
        try {
            ensureTableExists();
        } catch (RuntimeException e) {
            System.err.println("❌ " + e.getMessage());
            return;
        }

        String sql = "SELECT id, name, price FROM menu_items";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            ObservableList<MenuItem> tempList = FXCollections.observableArrayList();
            while (rs.next()) {
                tempList.add(new MenuItem(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
            }

            items.clear();
            items.addAll(tempList);

            // Thêm dữ liệu mẫu nếu bảng trống
            if (items.isEmpty() && !hasInitialized) {
                insertSampleData(conn);
                hasInitialized = true;
                loadFromDatabase(); // Reload sau khi thêm mẫu
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }

    private static void insertSampleData(Connection conn) {
        try {
            String[] sampleItems = {
                    "('Phở bò tái', 45000)",
                    "('Phở gà', 40000)",
                    "('Bún bò Huế', 50000)",
                    "('Mì Quảng', 45000)",
                    "('Cơm gà', 35000)",
                    "('Cơm sườn', 40000)",
                    "('Bún chả', 45000)",
                    "('Bánh mì thịt', 25000)",
                    "('Cà phê đen', 20000)",
                    "('Cà phê sữa', 25000)",
                    "('Trà đá', 5000)",
                    "('Trà chanh', 15000)",
                    "('Nước cam', 25000)",
                    "('Sinh tố bơ', 30000)",
                    "('Chè thái', 20000)",
                    "('Rau câu flan', 15000)",
                    "('Sữa chua', 15000)"
            };

            String sql = "INSERT INTO menu_items(name, price) VALUES " + String.join(", ", sampleItems);
            conn.createStatement().executeUpdate(sql);
            System.out.println("✅ Đã thêm dữ liệu mẫu vào bảng menu_items!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<MenuItem> getItems() {
        return items;
    }

    public static void addItem(MenuItem item) {
        ensureTableExists();
        String sql = "INSERT INTO menu_items(name, price) VALUES(?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.executeUpdate();

            // Lấy id được tạo tự động
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.setId(rs.getInt(1));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể thêm item: " + e.getMessage(), e);
        }
    }

    public static void updateItem(MenuItem item) {
        if (item.getId() == 0) {
            addItem(item);
            return;
        }

        ensureTableExists();
        String sql = "UPDATE menu_items SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setInt(3, item.getId());

            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Item không tồn tại trong database");
            }

            // Refresh trong list
            int index = items.indexOf(item);
            if (index >= 0) {
                items.set(index, item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể cập nhật item: " + e.getMessage(), e);
        }
    }

    public static void removeItem(MenuItem item) {
        if (item.getId() == 0) {
            items.remove(item);
            return;
        }

        ensureTableExists();
        String sql = "DELETE FROM menu_items WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getId());
            if (stmt.executeUpdate() > 0) {
                items.remove(item);
            } else {
                throw new RuntimeException("Item không tồn tại trong database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể xóa item: " + e.getMessage(), e);
        }
    }

    private static void ensureTableExists() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "menu_items", null)) {
                if (!rs.next()) {
                    String createTable = """
                            CREATE TABLE menu_items (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                price DOUBLE NOT NULL
                            )
                            """;
                    conn.createStatement().execute(createTable);
                    System.out.println("🧱 Đã tạo bảng menu_items!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tạo/kiểm tra bảng: " + e.getMessage(), e);
        }
    }

    public static ObservableList<MenuItem> getMainDishes() {
        return items
                .filtered(item -> item.getPrice() >= 35000 && !isDrink(item.getName()) && !isDessert(item.getName()));
    }

    public static ObservableList<MenuItem> getDrinks() {
        return items.filtered(item -> isDrink(item.getName()));
    }

    public static ObservableList<MenuItem> getDesserts() {
        return items.filtered(item -> isDessert(item.getName()));
    }

    private static boolean isDrink(String name) {
        String lowerName = name.toLowerCase();
        return lowerName.contains("cà phê") ||
                lowerName.contains("trà") ||
                lowerName.contains("nước") ||
                lowerName.contains("sinh tố");
    }

    private static boolean isDessert(String name) {
        String lowerName = name.toLowerCase();
        return lowerName.contains("chè") ||
                lowerName.contains("flan") ||
                lowerName.contains("sữa chua");
    }
}