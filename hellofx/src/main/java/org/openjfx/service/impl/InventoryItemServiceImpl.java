package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Employee;
import org.openjfx.entity.InventoryItem;
import org.openjfx.service.InventoryItemService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryItemServiceImpl implements InventoryItemService {
    @Override
    public List<InventoryItem> getAllInventoryItem() {
        List<InventoryItem> inventoryItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT InventoryItemID, ItemName, StockQuantity, UnitID, UnitPrice
                    FROM InventoryItem
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setInventoryItemID(rs.getInt("InventoryItemID"));
                inventoryItem.setItemName(rs.getString("ItemName"));
                inventoryItem.setStockQuantity(rs.getInt("StockQuantity"));
                inventoryItem.setUnitID(rs.getInt("UnitID"));
                inventoryItem.setUnitPrice(rs.getInt("UnitPrice"));
                inventoryItems.add(inventoryItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryItems;

    }
}
