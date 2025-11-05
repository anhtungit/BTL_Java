package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.Models.MenuItem;
import org.openjfx.service.MenuItemService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemServiceImpl implements MenuItemService {

    @Override
    public List<MenuItem> getAllMenuItem() {
        List<MenuItem> list = new ArrayList<>();

        String sql = "SELECT MenuItemID, ItemName, CurrentPrice FROM MenuItem";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("MenuItemID"),
                        rs.getString("ItemName"),
                        rs.getDouble("CurrentPrice"));
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
        MenuItem item = null;

        String sql = "SELECT MenuItemID, ItemName, CurrentPrice FROM MenuItem WHERE MenuItemID = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                item = new MenuItem(
                        rs.getInt("MenuItemID"),
                        rs.getString("ItemName"),
                        rs.getDouble("CurrentPrice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }



    @Override
    public void addMenuItem(MenuItem item) {
        String sql = "INSERT INTO MenuItem(ItemName, CurrentPrice) VALUES(?, ?)";

        try (Connection conn = DBConnection.getConnection();
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
        String sql = "UPDATE MenuItem SET ItemName = ?, CurrentPrice = ? WHERE MenuItemID = ?";

        try (Connection conn = DBConnection.getConnection();
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

        String sql = "DELETE FROM MenuItem WHERE MenuItemID = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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

            String sql = "INSERT INTO MenuItem(ItemName, CurrentPrice) VALUES " + String.join(", ", sampleItems);
            conn.createStatement().executeUpdate(sql);
            System.out.println(" Đã thêm dữ liệu mẫu vào bảng MenuItem!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
