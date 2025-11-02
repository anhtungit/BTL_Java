package org.openjfx.service;

import org.openjfx.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItem();
    MenuItem getMenuItemByMenuItemID(int menuItemID);
}
