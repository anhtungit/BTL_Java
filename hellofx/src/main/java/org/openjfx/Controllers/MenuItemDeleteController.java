package org.openjfx.Controllers;

import org.openjfx.Models.MenuItem;
import org.openjfx.Stores.MenuStore;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MenuItemDeleteController extends MenuItemControllerBase {

    @FXML
    private TableView<MenuItem> tableMenuItem;
    @FXML
    private TableColumn<MenuItem, String> colItemName;
    @FXML
    private TableColumn<MenuItem, String> colCurrentPrice;

    private ObservableList<MenuItem> menuItems;

    @FXML
    public void initialize() {
        reloadData();

        colItemName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colCurrentPrice.setCellValueFactory(
                cell -> new SimpleStringProperty(String.format("%.0f", cell.getValue().getPrice())));

        menuItems = MenuStore.getItems();
        tableMenuItem.setItems(menuItems);
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
        confirm.setContentText("Món: " + selected.getName());

        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
        confirm.getButtonTypes().setAll(yes, no);

        confirm.showAndWait().ifPresent(response -> {
            if (response == yes) {
                try {
                    String itemName = selected.getName();
                    MenuStore.removeItem(selected);
                    reloadData();
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
