package org.openjfx.Controllers;

import org.openjfx.Models.MenuItem;
import org.openjfx.Stores.MenuStore;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MenuItemViewController {

    @FXML private TableView<MenuItem> tableMenuItem;
    @FXML private TableColumn<MenuItem, String> colItemName;
    @FXML private TableColumn<MenuItem, Double> colCurrentPrice;

    @FXML
    private void initialize() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCurrentPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableMenuItem.setItems(MenuStore.getItems());
    }
}
