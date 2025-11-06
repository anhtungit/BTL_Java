package org.openjfx.Controllers;

import org.openjfx.entity.MenuItem;
import org.openjfx.service.MenuItemService;
import org.openjfx.service.impl.MenuItemServiceImpl;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MenuItemDeleteController extends MenuItemControllerBase {

    @FXML
    private TableView<MenuItem> tableMenuItem;
    @FXML
    private TableColumn<MenuItem, String> colItemName;
    @FXML
    private TableColumn<MenuItem, String> colCurrentPrice;



    MenuItemService menuItemService = new MenuItemServiceImpl();

    @FXML
    public void initialize() {
        // reloadData();
        colItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        colCurrentPrice.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        tableMenuItem.setItems(FXCollections.observableArrayList(menuItemService.getAllMenuItem()));
    }

    @FXML
    private void handleDelete() {
        MenuItem selected = tableMenuItem.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng chọn món cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Bạn có chắc muốn xóa món này?");
        confirm.setContentText("Món: " + selected.getItemName());

        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
        confirm.getButtonTypes().setAll(yes, no);

        confirm.showAndWait().ifPresent(response -> {
            if (response == yes) {
                try {
                    String itemName = selected.getItemName();
                    menuItemService.deleteMenuItem(selected.getMenuItemId());
                    // reloadData();
                    tableMenuItem.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công",
                            "Đã xóa món \"" + itemName + "\"!");
                } catch (RuntimeException e) {
                    handleDatabaseError(e);
                }
            }
        });
    }

    @FXML
    private void handleCancel() {
        tableMenuItem.getSelectionModel().clearSelection();
    }
}
