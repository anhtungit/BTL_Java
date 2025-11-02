package org.openjfx.entity;

import java.util.Date;

public class ImportNote {
    private int employeeID;
    private int inventoryItemID;
    private Date importDate;
    private int totalAmount;
    private int quantity;

    public ImportNote() {
    }

    public ImportNote(int employeeID, int inventoryItemID, Date importDate, int totalAmount, int quantity) {
        this.employeeID = employeeID;
        this.inventoryItemID = inventoryItemID;
        this.importDate = importDate;
        this.totalAmount = totalAmount;
        this.quantity = quantity;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getInventoryItemID() {
        return inventoryItemID;
    }

    public void setInventoryItemID(int inventoryItemID) {
        this.inventoryItemID = inventoryItemID;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
