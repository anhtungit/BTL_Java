package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Invoice;
import org.openjfx.service.InvoiceService;

import java.sql.*;
import java.time.LocalDate;

public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public Invoice getInvoiceByInvoiceID(int invoiceID) {
        Invoice invoice = new Invoice();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT InvoiceID, TotalAmount, CreatedAt, Status
                    FROM Invoice
                    WHERE InvoiceID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                invoice.setInvoiceID(rs.getInt("InvoiceID"));
                invoice.setTotalAmount(rs.getInt("TotalAmount"));
                invoice.setCreatedAt(rs.getDate("CreatedAt"));
                invoice.setStatus(rs.getInt("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoice;
    }

    @Override
    public int create() {
        int generatedKey = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO Invoice(TotalAmount, CreatedAt, Status)
                          VALUES (?, ?, ?)
                          """;
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 0);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setInt(3, 0);
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
    public void save(Invoice invoice) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE Invoice SET TotalAmount = ?, CreatedAt = ?, Status = ? WHERE InvoiceID = ?";
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setInt(1, invoice.getTotalAmount());
            ps.setDate(2, invoice.getCreatedAt());
            ps.setInt(3, invoice.getStatus());
            ps.setInt(4, invoice.getInvoiceID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Invoice invoice) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          DELETE FROM Invoice
                          WHERE InvoiceID = ?;
                          """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, invoice.getInvoiceID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int getTotalAmountByDate(LocalDate date) {
        int totalAmount = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT SUM(TotalAmount) AS TotalAmount
                    FROM Invoice
                    WHERE CreatedAt = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalAmount = rs.getInt("TotalAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
}