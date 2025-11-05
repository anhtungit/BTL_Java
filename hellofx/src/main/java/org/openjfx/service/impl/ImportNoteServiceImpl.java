package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.ImportNote;
import org.openjfx.service.ImportNoteService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
