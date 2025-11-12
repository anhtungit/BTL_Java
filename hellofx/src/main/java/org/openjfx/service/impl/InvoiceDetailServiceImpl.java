package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Invoice;
import org.openjfx.entity.InvoiceDetail;
import org.openjfx.service.InvoiceDetailService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailServiceImpl implements InvoiceDetailService {

    @Override
    public List<InvoiceDetail> getInvoiceDetailByInvoiceID(int invoiceID) {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT InvoiceID, MenuItemID, Quantity, PriceAtSale, lineTotal
                    FROM InvoiceDetail
                    WHERE InvoiceID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail();
                invoiceDetail.setInvoiceID(rs.getInt("InvoiceID"));
                invoiceDetail.setMenuItemID(rs.getInt("MenuItemID"));
                invoiceDetail.setQuantity(rs.getInt("Quantity"));
                invoiceDetail.setPriceAtSale(rs.getInt("PriceAtSale"));
                invoiceDetail.setLineTotal(rs.getInt("lineTotal"));
                invoiceDetails.add(invoiceDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceDetails;
    }

    @Override
    public void deleteAll(Invoice invoice) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          DELETE FROM InvoiceDetail
                          WHERE InvoiceID = ?;
                          """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, invoice.getInvoiceID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO InvoiceDetail (InvoiceID, MenuItemID, Quantity, PriceAtSale, lineTotal)
                          VALUES (?, ?, ?, ?, ?);
                          """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, invoiceDetail.getInvoiceID());
            ps.setInt(2, invoiceDetail.getMenuItemID());
            ps.setInt(3, invoiceDetail.getQuantity());
            ps.setInt(4, invoiceDetail.getPriceAtSale());
            ps.setInt(5, invoiceDetail.getLineTotal());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}