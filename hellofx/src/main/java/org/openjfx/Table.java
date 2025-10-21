package org.openjfx;

import javafx.beans.property.*;

public class Table {
    private final IntegerProperty tableNumber;
    private final StringProperty status; // "empty", "occupied", "reserved"
    private final StringProperty customerName;
    private final IntegerProperty capacity;
    private final StringProperty notes;

    public Table(int tableNumber, int capacity) {
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.status = new SimpleStringProperty("empty");
        this.customerName = new SimpleStringProperty("");
        this.capacity = new SimpleIntegerProperty(capacity);
        this.notes = new SimpleStringProperty("");
    }

    // Getters and Setters
    public int getTableNumber() {
        return tableNumber.get();
    }

    public IntegerProperty tableNumberProperty() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber.set(tableNumber);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public int getCapacity() {
        return capacity.get();
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity.set(capacity);
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

    public boolean isEmpty() {
        return "empty".equals(getStatus());
    }

    public boolean isOccupied() {
        return "occupied".equals(getStatus());
    }

    public boolean isReserved() {
        return "reserved".equals(getStatus());
    }

    @Override
    public String toString() {
        return "Bàn " + String.format("%02d", getTableNumber());
    }
}
