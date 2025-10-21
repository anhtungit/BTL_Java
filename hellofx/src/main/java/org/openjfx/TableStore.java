package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class TableStore {
    private static TableStore instance;
    private final ObservableList<Table> tables;
    private final ObservableList<Order> orders;

    private TableStore() {
        this.tables = FXCollections.observableArrayList();
        this.orders = FXCollections.observableArrayList();
        initializeTables();
    }

    public static TableStore getInstance() {
        if (instance == null) {
            instance = new TableStore();
        }
        return instance;
    }

    private void initializeTables() {
        // Tạo 20 bàn như trong ảnh
        for (int i = 1; i <= 20; i++) {
            Table table = new Table(i, 4); // Mỗi bàn có sức chứa 4 người
            tables.add(table);
        }
    }

    public ObservableList<Table> getTables() {
        return tables;
    }

    public ObservableList<Order> getOrders() {
        return orders;
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
            table.setStatus("occupied");
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
        Order order = getOrderByTable(fromTable);
        if (order != null) {
            order.setTableNumber(toTable);
            Table fromTableObj = getTable(fromTable);
            Table toTableObj = getTable(toTable);
            
            if (fromTableObj != null) {
                fromTableObj.setStatus("empty");
                fromTableObj.setCustomerName("");
            }
            
            if (toTableObj != null && toTableObj.isEmpty()) {
                toTableObj.setStatus("occupied");
                toTableObj.setCustomerName(order.getCustomerName());
            }
        }
    }

    public void splitTable(int tableNumber, List<Integer> newTables) {
        Order order = getOrderByTable(tableNumber);
        if (order != null) {
            // Tạo đơn hàng mới cho các bàn được tách
            for (int newTable : newTables) {
                Table newTableObj = getTable(newTable);
                if (newTableObj != null && newTableObj.isEmpty()) {
                    Order newOrder = new Order(
                            generateOrderId(),
                            newTable,
                            order.getCustomerName()
                    );
                    addOrder(newOrder);
                }
            }
        }
    }

    public void combineTables(List<Integer> tableNumbers, int targetTable) {
        // Gộp các đơn hàng từ nhiều bàn vào một bàn
        List<Order> ordersToCombine = new ArrayList<>();
        for (int tableNumber : tableNumbers) {
            Order order = getOrderByTable(tableNumber);
            if (order != null) {
                ordersToCombine.add(order);
            }
        }
        
        if (!ordersToCombine.isEmpty()) {
            Order combinedOrder = ordersToCombine.get(0);
            combinedOrder.setTableNumber(targetTable);
            
            // Gộp các món ăn từ các đơn hàng khác
            for (int i = 1; i < ordersToCombine.size(); i++) {
                Order orderToMerge = ordersToCombine.get(i);
                combinedOrder.getItems().addAll(orderToMerge.getItems());
                removeOrder(orderToMerge);
            }
            
            // Cập nhật trạng thái bàn
            for (int tableNumber : tableNumbers) {
                if (tableNumber != targetTable) {
                    Table table = getTable(tableNumber);
                    if (table != null) {
                        table.setStatus("empty");
                        table.setCustomerName("");
                    }
                }
            }
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

    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis();
    }

    public List<Table> getEmptyTables() {
        return tables.stream()
                .filter(Table::isEmpty)
                .toList();
    }

    public List<Table> getOccupiedTables() {
        return tables.stream()
                .filter(Table::isOccupied)
                .toList();
    }

    public List<Table> getReservedTables() {
        return tables.stream()
                .filter(Table::isReserved)
                .toList();
    }
}
