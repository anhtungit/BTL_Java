package org.openjfx.entity;

import java.util.Date;

public class Invoice {
    private int invoiceID;
    private int totalAmount;
    private Date createdAt;
    private int status;
    private int promotionID
    ;

    public Invoice() {
    }

    public Invoice(int invoiceID, int totalAmount, Date createdAt, int status, int promotionID) {
        this.invoiceID = invoiceID;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.status = status;
        this.promotionID = promotionID;
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

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }
}
