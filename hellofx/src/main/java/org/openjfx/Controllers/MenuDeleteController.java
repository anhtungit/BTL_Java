package org.openjfx.Controllers;

import org.openjfx.Models.MenuItem;
import org.openjfx.Stores.MenuStore;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MenuDeleteController {

    @FXML private TableView<MenuItem> tableMonAn;
    @FXML private TableColumn<MenuItem, String> colTenMon;
    @FXML private TableColumn<MenuItem, String> colGiaTien;

    private ObservableList<MenuItem> menuItems;

    @FXML
    public void initialize() {
        colTenMon.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colGiaTien.setCellValueFactory(cell ->
            new SimpleStringProperty(String.format("%.0f", cell.getValue().getPrice()))
        );

        menuItems = MenuStore.getItems();
        tableMonAn.setItems(menuItems);
    }

    @FXML
    private void handleXoa() {
        MenuItem selected = tableMonAn.getSelectionModel().getSelectedItem();

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
                MenuStore.removeItem(selected);
                tableMonAn.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa món \"" + selected.getName() + "\"!");
            }
        });
    }

    @FXML
    private void handleHuy() {
        tableMonAn.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
