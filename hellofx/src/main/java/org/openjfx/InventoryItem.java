package org.openjfx;

import javafx.beans.property.*;
import java.time.LocalDate;

public class InventoryItem {
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final StringProperty unit = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> importDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> exportDate = new SimpleObjectProperty<>();
    private final DoubleProperty total = new SimpleDoubleProperty();

    public InventoryItem(String name, int quantity, String unit, double price, LocalDate importDate) {
        setName(name);
        setQuantity(quantity);
        setUnit(unit);
        setPrice(price);
        setImportDate(importDate);
        updateTotal();
    }

    // Name
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Quantity
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int value) { 
        quantity.set(value);
        updateTotal();
    }
    public IntegerProperty quantityProperty() { return quantity; }

    // Unit
    public String getUnit() { return unit.get(); }
    public void setUnit(String value) { unit.set(value); }
    public StringProperty unitProperty() { return unit; }

    // Price
    public double getPrice() { return price.get(); }
    public void setPrice(double value) { 
        price.set(value);
        updateTotal();
    }
    public DoubleProperty priceProperty() { return price; }

    // Import Date
    public LocalDate getImportDate() { return importDate.get(); }
    public void setImportDate(LocalDate value) { importDate.set(value); }
    public ObjectProperty<LocalDate> importDateProperty() { return importDate; }

    // Export Date
    public LocalDate getExportDate() { return exportDate.get(); }
    public void setExportDate(LocalDate value) { exportDate.set(value); }
    public ObjectProperty<LocalDate> exportDateProperty() { return exportDate; }

    // Total
    public double getTotal() { return total.get(); }
    public DoubleProperty totalProperty() { return total; }

    private void updateTotal() {
        total.set(quantity.get() * price.get());
    }
}