package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Invoice;
import org.openjfx.service.InvoiceService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public Invoice getInvoiceByInvoiceID(int invoiceID) {
        Invoice invoice = null;
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
                invoice = new Invoice();
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
}
