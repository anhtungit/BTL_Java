package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class InventoryStore {
    private static InventoryStore instance;
    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();

    private InventoryStore() {
        // Initialize with fake data
        // Nguyên liệu nấu phở
        items.add(new InventoryItem("Phở khô", 50, "kg", 35000, LocalDate.now().minusDays(5)));
        items.add(new InventoryItem("Thịt bò tái", 20, "kg", 280000, LocalDate.now().minusDays(2)));
        items.add(new InventoryItem("Xương bò", 30, "kg", 80000, LocalDate.now().minusDays(3)));
        items.add(new InventoryItem("Hành tây", 15, "kg", 25000, LocalDate.now().minusDays(1)));
        items.add(new InventoryItem("Gừng", 5, "kg", 35000, LocalDate.now().minusDays(2)));
        
        // Đồ uống
        items.add(new InventoryItem("Cà phê hạt", 30, "kg", 180000, LocalDate.now().minusDays(10)));
        items.add(new InventoryItem("Trà túi lọc", 200, "gói", 2500, LocalDate.now().minusDays(15)));
        items.add(new InventoryItem("Sữa đặc", 48, "hộp", 25000, LocalDate.now().minusDays(20)));
        items.add(new InventoryItem("Syrup", 10, "chai", 85000, LocalDate.now().minusDays(25)));
        
        // Gia vị
        items.add(new InventoryItem("Muối", 25, "kg", 8000, LocalDate.now().minusDays(30)));
        items.add(new InventoryItem("Đường", 40, "kg", 20000, LocalDate.now().minusDays(30)));
        items.add(new InventoryItem("Bột ngọt", 10, "kg", 45000, LocalDate.now().minusDays(30)));
        items.add(new InventoryItem("Tiêu", 5, "kg", 250000, LocalDate.now().minusDays(30)));
        items.add(new InventoryItem("Tương ớt", 24, "chai", 15000, LocalDate.now().minusDays(45)));
        items.add(new InventoryItem("Nước mắm", 24, "chai", 35000, LocalDate.now().minusDays(45)));
        
        // Vật dụng
        items.add(new InventoryItem("Bát sứ", 100, "cái", 15000, LocalDate.now().minusDays(60)));
        items.add(new InventoryItem("Đũa inox", 150, "đôi", 12000, LocalDate.now().minusDays(60)));
        items.add(new InventoryItem("Ly thủy tinh", 80, "cái", 8000, LocalDate.now().minusDays(60)));
        items.add(new InventoryItem("Khăn giấy", 100, "gói", 12000, LocalDate.now().minusDays(15)));
        items.add(new InventoryItem("Ống hút", 1000, "cái", 100, LocalDate.now().minusDays(30)));
    }

    public static InventoryStore getInstance() {
        if (instance == null) {
            instance = new InventoryStore();
        }
        return instance;
    }

    public ObservableList<InventoryItem> getItems() {
        return items;
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public void removeItem(InventoryItem item) {
        items.remove(item);
    }

    public void updateItem(InventoryItem item, int newQuantity) {
        item.setQuantity(newQuantity);
    }

    public ObservableList<InventoryItem> getRunningLowItems() {
        return items.filtered(item -> {
            switch (item.getUnit().toLowerCase()) {
                case "kg":
                    return item.getQuantity() < 10;
                case "gói":
                case "hộp":
                case "chai":
                    return item.getQuantity() < 20;
                case "cái":
                case "đôi":
                    return item.getQuantity() < 50;
                default:
                    return false;
            }
        });
    }

    public ObservableList<InventoryItem> getExpensiveItems() {
        return items.filtered(item -> item.getPrice() > 100000);
    }

    public double getTotalInventoryValue() {
        return items.stream()
                   .mapToDouble(item -> item.getQuantity() * item.getPrice())
                   .sum();
    }
}