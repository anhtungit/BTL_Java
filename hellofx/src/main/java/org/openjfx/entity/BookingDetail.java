package org.openjfx.entity;

import java.util.Date;

public class BookingDetail {
    private int tableID;
    private int employeeID;
    private int invoiceID;
    private String customerName;
    private String customerPhone;
    private Date bookingDateTime;

    public BookingDetail() {
    }

    public BookingDetail(int tableID, int employeeID, int invoiceID, String customerName, String customerPhone, Date bookingDateTime) {
        this.tableID = tableID;
        this.employeeID = employeeID;
        this.invoiceID = invoiceID;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.bookingDateTime = bookingDateTime;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Date getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(Date bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }
}
