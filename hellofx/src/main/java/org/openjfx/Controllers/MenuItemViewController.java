package org.openjfx.Controllers;

import org.openjfx.entity.MenuItem;
import org.openjfx.service.MenuItemService;
import org.openjfx.service.impl.MenuItemServiceImpl;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MenuItemViewController {

    @FXML
    private TableView<MenuItem> tableMenuItem;
    @FXML
    private TableColumn<MenuItem, String> colItemName;
    @FXML
    private TableColumn<MenuItem, Double> colCurrentPrice;

    MenuItemService menuItemService = new MenuItemServiceImpl();

    @FXML
    private void initialize() {
        // MenuStore.loadFromDatabase();
        colItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        colCurrentPrice.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        tableMenuItem.setItems(FXCollections.observableArrayList(menuItemService.getAllMenuItem()));
    }
}
