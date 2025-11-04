package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Table;
import org.openjfx.service.TableService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableServiceImpl implements TableService {

    @Override
    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT TableID, StatusTable, TableName
                    FROM Tables
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Table table = new Table();
                table.setTableID(rs.getInt("TableID"));
                table.setStatus(rs.getString("StatusTable"));
                table.setTableName(rs.getString("TableName"));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public void changeStatusByTable(Table table) {
        table.setStatus("Trống".equals(table.getStatus())? "Đã đặt" : "Trống");
        save(table);
    }

    @Override
    public void save(Table table) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE Tables SET TableStatus = ? WHERE AccountID = ?";
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, table.getStatus());
            ps.setInt(2, table.getTableID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
