package org.openjfx.service;

import org.openjfx.entity.InventoryItem;

import java.util.List;

public interface InventoryItemService {
    List<InventoryItem> getAllInventoryItem();
    int create(InventoryItem inventoryItem);
    void save(InventoryItem inventoryItem);
    void delete(InventoryItem inventoryItem);
}
