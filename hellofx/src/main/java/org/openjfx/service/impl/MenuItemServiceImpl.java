package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.Models.MenuItem;
import org.openjfx.service.MenuItemService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemServiceImpl implements MenuItemService {

    private Connection getConnection() throws SQLException {
        Connection conn = DBConnection.getConnection();
        if (conn == null)
            throw new RuntimeException("Không thể kết nối database!");
        return conn;
    }


    @Override
    public List<MenuItem> getAllMenuItem() {
        ensureTableExists();
        ObservableList<MenuItem> list = FXCollections.observableArrayList();

        String sql = "SELECT id, name, price FROM menu_items";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"));
                list.add(item);
            }

            if (list.isEmpty()) {
                insertSampleData(conn);
                return getAllMenuItem();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public MenuItem getMenuItemByMenuItemID(int id) {
        ensureTableExists();
        MenuItem item = null;

        String sql = "SELECT id, name, price FROM menu_items WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }



    @Override
    public void addMenuItem(MenuItem item) {
        ensureTableExists();
        String sql = "INSERT INTO menu_items(name, price) VALUES(?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMenuItem(MenuItem item) {
        ensureTableExists();
        String sql = "UPDATE menu_items SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setInt(3, item.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMenuItem(int id) {
        ensureTableExists();
        String sql = "DELETE FROM menu_items WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void ensureTableExists() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, "menu_items", null);
            if (!rs.next()) {
                String createTable = """
                        CREATE TABLE menu_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            price DOUBLE NOT NULL
                        )
                        """;
                conn.createStatement().execute(createTable);
                System.out.println(" Đã tạo bảng menu_items!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể kiểm tra/tạo bảng: " + e.getMessage(), e);
        }
    }

    private void insertSampleData(Connection conn) {
        try {
            String[] sampleItems = {
                    "('Bò lúc lắc', 120000, 'Món chính')",
                    "('Canh chua cá', 90000, 'Món chính')",
                    "('Cơm chiên trứng', 45000, 'Món phụ')",
                    "('Bia Heineken', 30000, 'Đồ uống')",
                    "('Trà đá', 5000, 'Đồ uống')"
            };

            String sql = "INSERT INTO menu_items(name, price) VALUES " + String.join(", ", sampleItems);
            conn.createStatement().executeUpdate(sql);
            System.out.println(" Đã thêm dữ liệu mẫu vào bảng menu_items!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
