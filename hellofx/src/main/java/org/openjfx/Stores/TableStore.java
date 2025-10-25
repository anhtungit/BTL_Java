package org.openjfx.Stores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;

import org.openjfx.Models.Order;
import org.openjfx.Models.OrderItem;
import org.openjfx.Models.Table;

import java.util.HashMap;

public class TableStore {
    private static TableStore instance;
    private final ObservableList<Table> tables;
    private final ObservableList<Order> orders;
    // Lưu danh sách món theo từng bàn (đơn giản hoá thay cho Order)
    private final Map<Integer, List<OrderItem>> tableNumberToItems;

    private TableStore() {
        this.tables = FXCollections.observableArrayList();
        this.orders = FXCollections.observableArrayList();
        this.tableNumberToItems = new HashMap<>();
        initializeTables();
    }

    public static TableStore getInstance() {
        if (instance == null) {
            instance = new TableStore();
        }
        return instance;
    }

    private void initializeTables() {
        for (int i = 1; i <= 20; i++) {
            Table table = new Table(i, 4);
            tables.add(table);
        }
        
        reserveTable(3, "Nguyễn Văn A");
        reserveTable(7, "Trần Thị B");
        reserveTable(12, "Lê Văn C");
        reserveTable(15, "Phạm Thị D");
        reserveTable(18, "Hoàng Văn E");
    }

    public ObservableList<Table> getTables() {
        return tables;
    }

    public ObservableList<Order> getOrders() {
        return orders;
    }

    // --- API cho món đã chọn của từng bàn ---
    public List<OrderItem> getItemsForTable(int tableNumber) {
        return tableNumberToItems.getOrDefault(tableNumber, FXCollections.observableArrayList());
    }

    public void setItemsForTable(int tableNumber, List<OrderItem> items) {
        tableNumberToItems.put(tableNumber, items);
    }

    public Table getTable(int tableNumber) {
        return tables.stream()
                .filter(table -> table.getTableNumber() == tableNumber)
                .findFirst()
                .orElse(null);
    }

    public Order getOrderByTable(int tableNumber) {
        return orders.stream()
                .filter(order -> order.getTableNumber() == tableNumber && !order.isPaid())
                .findFirst()
                .orElse(null);
    }

    public void addOrder(Order order) {
        orders.add(order);
        Table table = getTable(order.getTableNumber());
        if (table != null) {
            table.setStatus("reserved");
            table.setCustomerName(order.getCustomerName());
        }
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        Table table = getTable(order.getTableNumber());
        if (table != null) {
            table.setStatus("empty");
            table.setCustomerName("");
        }
    }

    public void reserveTable(int tableNumber, String customerName) {
        Table table = getTable(tableNumber);
        if (table != null && table.isEmpty()) {
            table.setStatus("reserved");
            table.setCustomerName(customerName);
        }
    }

    public void cancelReservation(int tableNumber) {
        Table table = getTable(tableNumber);
        if (table != null && table.isReserved()) {
            table.setStatus("empty");
            table.setCustomerName("");
        }
    }

    public void transferTable(int fromTable, int toTable) {
        Table fromTableObj = getTable(fromTable);
        Table toTableObj = getTable(toTable);
        
        if (fromTableObj == null || toTableObj == null || !toTableObj.isEmpty()) {
            return;
        }

        // Chuyển thông tin đơn hàng (nếu có)
        Order order = getOrderByTable(fromTable);
        if (order != null) {
            order.setTableNumber(toTable);
        }

        // Chuyển thông tin bàn
        String customerName = fromTableObj.getCustomerName();
        String status = fromTableObj.getStatus();
        
        // Cập nhật bàn mới
        toTableObj.setStatus(status);
        toTableObj.setCustomerName(customerName);
        
        // Đặt lại bàn cũ thành trống
        fromTableObj.setStatus("empty");
        fromTableObj.setCustomerName("");
        
        // Chuyển dữ liệu món ăn từ bàn cũ sang bàn mới
        java.util.List<OrderItem> itemsFromOldTable = getItemsForTable(fromTable);
        if (itemsFromOldTable != null && !itemsFromOldTable.isEmpty()) {
            setItemsForTable(toTable, itemsFromOldTable);
            setItemsForTable(fromTable, new java.util.ArrayList<>()); // Xóa dữ liệu bàn cũ
        }
    }


    public void payOrder(Order order) {
        order.setStatus("paid");
        Table table = getTable(order.getTableNumber());
        if (table != null) {
            table.setStatus("empty");
            table.setCustomerName("");
        }
    }


    public List<Table> getEmptyTables() {
        return tables.stream()
                .filter(Table::isEmpty)
                .toList();
    }


    public List<Table> getReservedTables() {
        return tables.stream()
                .filter(Table::isReserved)
                .toList();
    }
}
