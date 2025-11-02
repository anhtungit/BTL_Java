package org.openjfx.entity;

public class MenuItemRecipe {
    private int menuItemID;
    private int inventoryItemID;
    private int amount;
    private String unitName;

    public MenuItemRecipe() {
    }

    public MenuItemRecipe(int menuItemID, int inventoryItemID, int amount, String unitName) {
        this.menuItemID = menuItemID;
        this.inventoryItemID = inventoryItemID;
        this.amount = amount;
        this.unitName = unitName;
    }

    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public int getInventoryItemID() {
        return inventoryItemID;
    }

    public void setInventoryItemID(int inventoryItemID) {
        this.inventoryItemID = inventoryItemID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
