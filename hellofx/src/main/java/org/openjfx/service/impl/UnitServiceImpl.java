package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Unit;
import org.openjfx.service.UnitService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitServiceImpl implements UnitService {


    @Override
    public Unit getUnitByUnitID(int unitID) {
        Unit unit = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT UnitID, UnitName
                    FROM Unit
                    WHERE UnitID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, unitID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                unit = new Unit();
                unit.setUnitId(rs.getInt("UnitID"));
                unit.setUnitName(rs.getString("UnitName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unit;
    }
}
