package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Invoice;
import org.openjfx.entity.MenuItem;
import org.openjfx.entity.Table;
import org.openjfx.service.MenuItemService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemServiceImpl implements MenuItemService {
    @Override
    public List<MenuItem> getAllMenuItem() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT MenuItemID, ItemName, CurrentPrice, ItemType
                    FROM MenuItem
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setMenuItemId(rs.getInt("MenuItemID"));
                menuItem.setItemName(rs.getString("ItemName"));
                menuItem.setCurrentPrice(rs.getInt("CurrentPrice"));
                menuItem.setCurrentPrice(rs.getInt("ItemType"));
                menuItems.add(menuItem);
            }
        } catch (SQLException e) {
        }
        return menuItems;
    }

    @Override
    public MenuItem getMenuItemByMenuItemID(int menuItemID) {
        MenuItem menuItem = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT MenuItemID, ItemName, CurrentPrice, ItemType
                    FROM MenuItem
                    Where MenuItemID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, menuItemID);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                menuItem = new MenuItem();
                menuItem.setMenuItemId(rs.getInt("MenuItemID"));
                menuItem.setItemName(rs.getString("ItemName"));
                menuItem.setCurrentPrice(rs.getInt("CurrentPrice"));
                menuItem.setCurrentPrice(rs.getInt("ItemType"));
            }
        } catch (SQLException e) {
        }
        return menuItem;
    }
}
