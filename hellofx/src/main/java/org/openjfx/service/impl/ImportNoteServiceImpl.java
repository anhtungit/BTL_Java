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
//        try (Connection conn = DBConnection.getConnection()) {
//            String sql = """
//                    SELECT EmployeeID, PositionID, FullName, PhoneNumber, Address
//                    FROM Employee
//                    WHERE AccountID = ?
//                    """;
//
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, accountId);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                employee.setEmployeeID(rs.getInt("EmployeeID"));
//                employee.setPositionID(rs.getInt("PositionID"));
//                employee.setAccountID(accountId);
//                employee.setFullName(rs.getString("FullName"));
//                employee.setPhoneNumber(rs.getString("PhoneNumber"));
//                employee.setAddress(rs.getString("Address"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        return importNote;
    }
}
