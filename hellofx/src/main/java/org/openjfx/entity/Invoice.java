package org.openjfx.entity;


import java.sql.Date;

public class Invoice {
    private int invoiceID;
    private int totalAmount;
    private Date createdAt;
    private int status;

    public Invoice() {
    }

    public Invoice(int totalAmount, Date createdAt, int status) {
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Invoice(int invoiceID, int totalAmount, Date createdAt, int status) {
        this.invoiceID = invoiceID;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.status = status;

    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
