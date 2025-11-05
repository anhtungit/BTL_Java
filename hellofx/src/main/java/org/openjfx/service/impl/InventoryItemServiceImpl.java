package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.InventoryItem;
import org.openjfx.service.InventoryItemService;

import java.sql.*;
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
                if (rs.getInt("UnitID") == 21)
                    continue;
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

    @Override
    public int create(InventoryItem inventoryItem) {
        int generatedKey = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO InventoryItem(ItemName, StockQuantity, UnitID, UnitPrice)
                          VALUES (?, ?, ?, ?)
                          """;

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, inventoryItem.getItemName());
            ps.setInt(2, inventoryItem.getStockQuantity());
            ps.setInt(3, inventoryItem.getUnitID());
            ps.setInt(4, inventoryItem.getUnitPrice());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedKey = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedKey;
    }

    @Override
    public void save(InventoryItem inventoryItem) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE InventoryItem SET ItemName = ?, StockQuantity = ?, UnitID = ?, UnitPrice = ? WHERE InventoryItemID = ?";
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, inventoryItem.getItemName());
            ps.setInt(2, inventoryItem.getStockQuantity());
            ps.setInt(3, inventoryItem.getUnitID());
            ps.setInt(4, inventoryItem.getUnitPrice());
            ps.setInt(5, inventoryItem.getInventoryItemID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(InventoryItem inventoryItem) {
        inventoryItem.setUnitID(21);
        save(inventoryItem);
    }
}
