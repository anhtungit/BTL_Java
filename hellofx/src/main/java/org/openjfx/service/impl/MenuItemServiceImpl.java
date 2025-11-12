package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.MenuItem;
import org.openjfx.service.MenuItemService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemServiceImpl implements MenuItemService {

    @Override
    public List getAllMenuItem() {
        List<MenuItem> list = new ArrayList<>();

        String sql = "SELECT MenuItemID, ItemName, CurrentPrice, ItemType FROM MenuItem";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                if (rs.getString("ItemType").equals("Đã xoá")) {
                    continue;
                }

                MenuItem item = new MenuItem();
                item.setMenuItemId(rs.getInt("MenuItemID"));
                item.setItemName(rs.getString("ItemName"));
                item.setCurrentPrice(rs.getInt("CurrentPrice"));
                item.setItemType(rs.getString("ItemType"));
                list.add(item);
            }

            if (list.isEmpty()) {
                return getAllMenuItem();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public MenuItem getMenuItemByMenuItemID(int id) {
        MenuItem item = new MenuItem();

        String sql = "SELECT MenuItemID, ItemName, CurrentPrice, ItemType FROM MenuItem WHERE MenuItemID = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
               
                item.setMenuItemId(rs.getInt("MenuItemID"));
                item.setItemName(rs.getString("ItemName"));
                item.setCurrentPrice(rs.getInt("CurrentPrice"));
                item.setItemType(rs.getString("ItemType"));
              
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }



    @Override
    public void addMenuItem(MenuItem item) {
        String sql = "INSERT INTO MenuItem(ItemName, CurrentPrice, ItemType) VALUES(?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getItemName());
            stmt.setDouble(2, item.getCurrentPrice());
            stmt.setString(3, filterType(item));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.setMenuItemId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMenuItem(MenuItem item) {
        String sql = "UPDATE MenuItem SET ItemName = ?, CurrentPrice = ?, ItemType = ? WHERE MenuItemID = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setDouble(2, item.getCurrentPrice());
            stmt.setString(3, filterType(item));
            stmt.setInt(4, item.getMenuItemId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMenuItem(int id) {

        String sql = "UPDATE MenuItem SET ItemType = ? WHERE MenuItemID = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Đã xoá");
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String filterType(MenuItem item){
        if(item.getItemName().contains("Trà") || item.getItemName().contains("Cà phê") || item.getItemName().contains("Nước")){
            return "Đồ uống";
        }
        return "Đồ ăn";
    }
    

}
