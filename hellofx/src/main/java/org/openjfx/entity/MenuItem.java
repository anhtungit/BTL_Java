package org.openjfx.entity;

public class MenuItem {
    private int menuItemId;
    private String ItemName;
    private int currentPrice;
    private String itemType;

    public MenuItem() {
    }

    public MenuItem(int menuItemId, String itemName, int currentPrice, String itemType) {
        this.menuItemId = menuItemId;
        ItemName = itemName;
        this.currentPrice = currentPrice;
        this.itemType = itemType;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
