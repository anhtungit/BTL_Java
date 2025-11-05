package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.ExportNote;
import org.openjfx.entity.ImportNote;
import org.openjfx.service.ExportNoteService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportNoteImpl implements ExportNoteService {
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
}
