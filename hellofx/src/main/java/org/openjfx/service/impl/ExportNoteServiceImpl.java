package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.ExportNote;
import org.openjfx.service.ExportNoteService;

import java.sql.*;

public class ExportNoteServiceImpl implements ExportNoteService {
    @Override
    public ExportNote getExportNoteByInventoryID(int inventoryID) {
        ExportNote exportNote = new ExportNote();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT InventoryItemID, ExportNoteID, EmployeeID, TotalExportAmount, ExportDate, Quantity
                    FROM ExportNote
                    WHERE InventoryItemID = ?
                    ORDER BY ExportDate DESC
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, inventoryID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exportNote.setExportNoteID(rs.getInt("ExportNoteID"));
                exportNote.setEmployeeID(rs.getInt("EmployeeID"));
                exportNote.setTotalExportAmount(rs.getInt("TotalExportAmount"));
                exportNote.setExportDate(rs.getDate("ExportDate"));
                exportNote.setQuantity(rs.getInt("Quantity"));
                exportNote.setInventoryItemID(rs.getInt("InventoryItemID"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exportNote;
    }

    @Override
    public int create(ExportNote exportNote) {
        int generatedKey = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO ExportNote(EmployeeID, InventoryItemID, TotalExportAmount, ExportDate, Quantity)
                          VALUES (?, ?, ?, ?, ?)
                          """;

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, exportNote.getEmployeeID());
            ps.setInt(2, exportNote.getInventoryItemID());
            ps.setInt(3, exportNote.getTotalExportAmount());
            ps.setDate(4, exportNote.getExportDate());
            ps.setInt(5, exportNote.getQuantity());

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
