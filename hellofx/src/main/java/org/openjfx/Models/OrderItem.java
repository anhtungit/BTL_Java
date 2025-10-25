package org.openjfx.Models;

import javafx.beans.property.*;

public class OrderItem {
    private final StringProperty itemName;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final StringProperty notes;

    public OrderItem(String itemName, double price, int quantity) {
        this.itemName = new SimpleStringProperty(itemName);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.notes = new SimpleStringProperty("");
    }

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public double getSubtotal() {
        return getPrice() * getQuantity();
    }

    @Override
    public String toString() {
        return getItemName() + " x" + getQuantity() + " - " + String.format("%.0f VND", getSubtotal());
    }
}
