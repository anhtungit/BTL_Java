package org.openjfx.Models;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final StringProperty orderId;
    private final IntegerProperty tableNumber;
    private final StringProperty customerName;
    private final ObjectProperty<LocalDateTime> orderTime;
    private final StringProperty status;
    private final DoubleProperty totalAmount;
    private final List<OrderItem> items;
    private final StringProperty notes;

    public Order(String orderId, int tableNumber, String customerName) {
        this.orderId = new SimpleStringProperty(orderId);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.customerName = new SimpleStringProperty(customerName);
        this.orderTime = new SimpleObjectProperty<>(LocalDateTime.now());
        this.status = new SimpleStringProperty("pending");
        this.totalAmount = new SimpleDoubleProperty(0.0);
        this.items = new ArrayList<>();
        this.notes = new SimpleStringProperty("");
    }

    public String getOrderId() {
        return orderId.get();
    }

    public StringProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId.set(orderId);
    }

    public int getTableNumber() {
        return tableNumber.get();
    }

    public IntegerProperty tableNumberProperty() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber.set(tableNumber);
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

    public LocalDateTime getOrderTime() {
        return orderTime.get();
    }

    public ObjectProperty<LocalDateTime> orderTimeProperty() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime.set(orderTime);
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

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public List<OrderItem> getItems() {
        return items;
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

    public void addItem(OrderItem item) {
        items.add(item);
        calculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        calculateTotal();
    }

    private void calculateTotal() {
        double total = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        setTotalAmount(total);
    }

    public boolean isPending() {
        return "pending".equals(getStatus());
    }

    public boolean isConfirmed() {
        return "confirmed".equals(getStatus());
    }

    public boolean isServed() {
        return "served".equals(getStatus());
    }

    public boolean isPaid() {
        return "paid".equals(getStatus());
    }
}
