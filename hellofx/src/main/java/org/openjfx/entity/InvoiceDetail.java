package org.openjfx.entity;

public class InvoiceDetail {
    private int invoiceID;
    private int menuItemID;
    private int quantity;
    private int priceAtSale;
    private int lineTotal;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int invoiceID, int menuItemID, int quantity, int priceAtSale, int lineTotal) {
        this.invoiceID = invoiceID;
        this.menuItemID = menuItemID;
        this.quantity = quantity;
        this.priceAtSale = priceAtSale;
        this.lineTotal = lineTotal;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriceAtSale() {
        return priceAtSale;
    }

    public void setPriceAtSale(int priceAtSale) {
        this.priceAtSale = priceAtSale;
    }

    public int getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(int lineTotal) {
        this.lineTotal = lineTotal;
    }
}
