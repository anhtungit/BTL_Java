package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.BookingDetail;
import org.openjfx.entity.Invoice;
import org.openjfx.entity.Table;
import org.openjfx.service.BookingDetailService;
import org.openjfx.service.InvoiceService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDetailServiceImpl implements BookingDetailService {

    InvoiceService invoiceService = new InvoiceServiceImpl();

    @Override
    public BookingDetail getBookingDetailNewlestByTableID(int tableID) {
        BookingDetail bookingDetail = new BookingDetail();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT TableID, EmployeeID, InvoiceID, CustomerName, CustomerPhone, BookingDateTime
                    FROM BookingDetail
                    WHERE TableID = ?
                    ORDER BY InvoiceID DESC
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tableID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (invoiceService.getInvoiceByInvoiceID(bookingDetail.getInvoiceID()).getStatus() == 1)
                    break;
                bookingDetail.setTableID(rs.getInt("TableID"));
                bookingDetail.setEmployeeID(rs.getInt("EmployeeID"));
                bookingDetail.setInvoiceID(rs.getInt("InvoiceID"));
                bookingDetail.setCustomerName(rs.getString("CustomerName"));
                bookingDetail.setCustomerPhone(rs.getString("CustomerPhone"));
                bookingDetail.setBookingDateTime(rs.getDate("BookingDateTime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingDetail;
    }

    @Override
    public void changeTableInBookingDetail(Table sourceTable, Table destTable) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    UPDATE BookingDetail Set TableID = ? WHERE InvoiceID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, destTable.getTableID());
            int invoiceIDOfSrcTable = getBookingDetailNewlestByTableID(sourceTable.getTableID()).getInvoiceID();
            ps.setInt(2, invoiceIDOfSrcTable);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(BookingDetail bookingDetail) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    DELETE FROM BookingDetail WHERE InvoiceID = ? AND TableID = ? AND EmployeeID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingDetail.getInvoiceID());
            ps.setInt(2, bookingDetail.getTableID());
            ps.setInt(3, bookingDetail.getEmployeeID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(BookingDetail bookingDetail) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO BookingDetail(TableID, EmployeeID, InvoiceID, CustomerName, CustomerPhone, BookingDateTime)
                          VALUES (?, ?, ?, ?, ?, ?)
                          """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingDetail.getTableID());
            ps.setInt(2, bookingDetail.getEmployeeID());
            ps.setInt(3, bookingDetail.getInvoiceID());
            ps.setString(4, bookingDetail.getCustomerName());
            ps.setString(5, bookingDetail.getCustomerPhone());
            ps.setDate(6, bookingDetail.getBookingDateTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Invoice> getAllInvoicesByEmployeeID(int employeeID) {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT i.InvoiceID, i.TotalAmount, i.CreatedAt, i.Status
                    FROM Invoice i
                    JOIN BookingDetail bd ON i.InvoiceID = bd.InvoiceID
                    WHERE bd.EmployeeID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceID(rs.getInt("InvoiceID"));
                invoice.setTotalAmount(rs.getInt("TotalAmount"));
                invoice.setCreatedAt(rs.getDate("CreatedAt"));
                invoice.setStatus(rs.getInt("Status"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
}