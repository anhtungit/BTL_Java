package org.openjfx.entity;

import java.util.Date;

public class ExportNote {
    private int exportNoteID;
    private int employeeID;
    private int inventoryItemID;
    private int totalExportAmount;
    private Date exportDate;
    private int quantity;

    public ExportNote() {
    }

    public ExportNote(int exportNoteID, int employeeID, int inventoryItemID, int totalExportAmount, Date exportDate, int quantity) {
        this.exportNoteID = exportNoteID;
        this.employeeID = employeeID;
        this.inventoryItemID = inventoryItemID;
        this.totalExportAmount = totalExportAmount;
        this.exportDate = exportDate;
        this.quantity = quantity;
    }

    public int getExportNoteID() {
        return exportNoteID;
    }

    public void setExportNoteID(int exportNoteID) {
        this.exportNoteID = exportNoteID;
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

    public int getTotalExportAmount() {
        return totalExportAmount;
    }

    public void setTotalExportAmount(int totalExportAmount) {
        this.totalExportAmount = totalExportAmount;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
