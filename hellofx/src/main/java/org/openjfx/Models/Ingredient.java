package org.openjfx.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ingredient {

    private final StringProperty inventoryItemName;
    private final StringProperty amount;
    private final StringProperty unitName;

    public Ingredient(String inventoryItemName, String amount, String unitName) {
        this.inventoryItemName = new SimpleStringProperty(inventoryItemName);
        this.amount = new SimpleStringProperty(amount);
        this.unitName = new SimpleStringProperty(unitName);
    }

    public String getInventoryItemName() { 
        return inventoryItemName.get(); 
    }

    public void setInventoryItemName(String value) { 
        inventoryItemName.set(value); 
    }

    public StringProperty inventoryItemNameProperty() { 
        return inventoryItemName; 
    }

    public String getAmount() { 
        return amount.get(); 
    }

    public void setAmount(String value) { 
        amount.set(value); 
    }

    public StringProperty amountProperty() { 
        return amount; 
    }

    public String getUnitName() { 
        return unitName.get(); 
    }

    public void setUnitName(String value) { 
        unitName.set(value); 
    }

    public StringProperty unitNameProperty() { 
        return unitName; 
    }
}
