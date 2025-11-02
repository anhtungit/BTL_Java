
package org.openjfx.service;

import org.openjfx.Models.MenuItem;
import java.util.List;

public interface MenuItemService {

    List<MenuItem> getAllMenuItem();

    MenuItem getMenuItemByMenuItemID(int id);

    void addMenuItem(MenuItem item);

    void updateMenuItem(MenuItem item);

    void deleteMenuItem(int id);
}
