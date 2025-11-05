package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.ImportNote;
import org.openjfx.service.ImportNoteService;

import java.sql.*;

public class ImportNoteServiceImpl implements ImportNoteService {
    @Override
    public ImportNote getImportNoteByInventoryID(int inventoryID) {
        ImportNote importNote = new ImportNote();
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
    public void create(ImportNote importNote) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO ImportNote(ImportDate, InventoryItemID, TotalAmount, Quantity, EmployeeID)
                          VALUES (?, ?, ?, ?, ?)
                          """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, importNote.getImportDate());
            ps.setInt(2, importNote.getInventoryItemID());
            ps.setInt(3, importNote.getTotalAmount());
            ps.setInt(4, importNote.getQuantity());
            ps.setInt(5, importNote.getEmployeeID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(ImportNote importNote) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE ImportNote SET ImportDate = ?, TotalAmount = ?, Quantity = ? WHERE EmployeeID = ? AND InventoryItemID = ?";
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setDate(1, importNote.getImportDate());
            ps.setInt(2, importNote.getTotalAmount());
            ps.setInt(3, importNote.getQuantity());
            ps.setInt(4, importNote.getEmployeeID());
            ps.setInt(5, importNote.getInventoryItemID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
