package org.openjfx.entity;

public class InventoryItem {
    private int inventoryItemID;
    private String itemName;
    private int stockQuantity;
    private int unitID;
    private int unitPrice;

    public InventoryItem() {
    }

    public InventoryItem(String itemName, int stockQuantity, int unitID, int unitPrice) {
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.unitID = unitID;
        this.unitPrice = unitPrice;
    }

    public InventoryItem(int inventoryItemID, String itemName, int stockQuantity, int unitID, int unitPrice) {
        this.inventoryItemID = inventoryItemID;
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.unitID = unitID;
        this.unitPrice = unitPrice;
    }

    public int getInventoryItemID() {
        return inventoryItemID;
    }

    public void setInventoryItemID(int inventoryItemID) {
        this.inventoryItemID = inventoryItemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
}
