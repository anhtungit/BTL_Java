package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.BookingDetail;
import org.openjfx.service.BookingDetailService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDetailServiceImpl implements BookingDetailService {

    @Override
    public BookingDetail getBookingDetailByTableID(int tableID) {
        BookingDetail bookingDetail = null;
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
            if (rs.next()) {
                bookingDetail = new BookingDetail();
                bookingDetail.setTableID(rs.getInt("TableID"));
                bookingDetail.setEmployeeID(rs.getInt("EmployeeID"));
                bookingDetail.setInvoiceID(rs.getInt("InvoiceID"));
                bookingDetail.setCustomerName(rs.getString("CustomerName"));
                bookingDetail.setCustomerPhone(rs.getString("CustomerPhone"));
                bookingDetail.setBookingDateTime(rs.getDate("BookingDateTime"));
            }
        } catch (SQLException e) {
        }
        return bookingDetail;
    }
}
