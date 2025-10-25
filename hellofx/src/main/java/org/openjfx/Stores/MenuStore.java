package org.openjfx.Stores;

import org.openjfx.Models.MenuItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MenuStore {
    private static final ObservableList<MenuItem> items = FXCollections.observableArrayList();

    static {
        items.add(new MenuItem("Phở bò tái", 45000));
        items.add(new MenuItem("Phở gà", 40000));
        items.add(new MenuItem("Bún bò Huế", 50000));
        items.add(new MenuItem("Mì Quảng", 45000));
        items.add(new MenuItem("Cơm gà", 35000));
        items.add(new MenuItem("Cơm sườn", 40000));
        items.add(new MenuItem("Bún chả", 45000));
        items.add(new MenuItem("Bánh mì thịt", 25000));

        items.add(new MenuItem("Cà phê đen", 20000));
        items.add(new MenuItem("Cà phê sữa", 25000));
        items.add(new MenuItem("Trà đá", 5000));
        items.add(new MenuItem("Trà chanh", 15000));
        items.add(new MenuItem("Nước cam", 25000));
        items.add(new MenuItem("Sinh tố bơ", 30000));
        
        items.add(new MenuItem("Chè thái", 20000));
        items.add(new MenuItem("Rau câu flan", 15000));
        items.add(new MenuItem("Sữa chua", 15000));
    }

    public static ObservableList<MenuItem> getItems() {
        return items;
    }

    public static void addItem(MenuItem item) {
        items.add(item);
    }

    public static void removeItem(MenuItem item) {
        items.remove(item);
    }
    
    public static ObservableList<MenuItem> getMainDishes() {
        return items.filtered(item -> 
            item.getPrice() >= 35000 && !isDrink(item.getName()) && !isDessert(item.getName()));
    }

    public static ObservableList<MenuItem> getDrinks() {
        return items.filtered(item -> isDrink(item.getName()));
    }

    public static ObservableList<MenuItem> getDesserts() {
        return items.filtered(item -> isDessert(item.getName()));
    }

    private static boolean isDrink(String name) {
        String lowerName = name.toLowerCase();
        return lowerName.contains("cà phê") || 
               lowerName.contains("trà") || 
               lowerName.contains("nước") ||
               lowerName.contains("sinh tố");
    }

    private static boolean isDessert(String name) {
        String lowerName = name.toLowerCase();
        return lowerName.contains("chè") || 
               lowerName.contains("flan") || 
               lowerName.contains("sữa chua");
    }
}