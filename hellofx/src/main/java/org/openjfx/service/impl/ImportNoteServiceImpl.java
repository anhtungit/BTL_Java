package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.ImportNote;
import org.openjfx.service.ImportNoteService;

import java.sql.*;

public class ImportNoteServiceImpl implements ImportNoteService {
    @Override
    public ImportNote getImportNoteByInventoryID(int inventoryID) {
        ImportNote importNote = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT EmployeeID, ImportDate, InventoryItemID, TotalAmount, Quantity
                    FROM ImportNote
                    WHERE InventoryItemID = ?
                    ORDER BY ImportDate DESC
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, inventoryID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                importNote = new ImportNote();
                importNote.setEmployeeID(rs.getInt("EmployeeID"));
                importNote.setImportDate(rs.getDate("ImportDate"));
                importNote.setTotalAmount(rs.getInt("TotalAmount"));
                importNote.setQuantity(rs.getInt("Quantity"));
                importNote.setInventoryItemID(rs.getInt("InventoryItemID"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return importNote;
    }

    @Override
    public int create(ImportNote importNote) {
        int generatedKey = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO ImportNote(ImportDate, InventoryItemID, TotalAmount, Quantity)
                          VALUES (?, ?, ?, ?)
                          """;
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, importNote.getImportDate());
            ps.setInt(2, importNote.getInventoryItemID());
            ps.setInt(3, importNote.getTotalAmount());
            ps.setInt(4, importNote.getQuantity());
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
}
