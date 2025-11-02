package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Position;
import org.openjfx.service.PositionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PositionServiceImpl implements PositionService {
    @Override
    public Position getPositionByPositionID(int id) {
        Position position = new Position();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT PositionName, Salary
                    FROM Position
                    WHERE PositionId = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                position.setPositionId(id);
                position.setPositionName(rs.getString("PositionName"));
                position.setSalary(rs.getInt("Salary"));
            }
        } catch (SQLException e) {
        }
        return position;
    }

    @Override
    public Position getPositionByPositionName(String positionName) {
        Position position = new Position();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT PositionID, Salary
                    FROM Position
                    WHERE positionName = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, positionName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                position.setPositionId(rs.getInt("PositionID"));
                position.setPositionName(positionName);
                position.setSalary(rs.getInt("Salary"));
            }
        } catch (SQLException e) {
        }
        return position;
    }

    @Override
    public List<Position> getAllPosition() {
        List<Position> positions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT PositionID, PositionName, Salary
                    FROM Position
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Position position = new Position();
                position.setPositionId(rs.getInt("PositionID"));
                position.setPositionName(rs.getString("PositionName"));
                position.setSalary(rs.getInt("Salary"));
                positions.add(position);
            }
        } catch (SQLException e) {
        }
        return positions;
    }

}
