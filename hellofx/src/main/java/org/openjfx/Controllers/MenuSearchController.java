package org.openjfx.Controllers;

import org.openjfx.Models.MenuItem;
import org.openjfx.Stores.MenuStore;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MenuSearchController {

    @FXML private TextField txtTimKiem;
    @FXML private TableView<MenuItem> tableKetQua;
    @FXML private TableColumn<MenuItem, String> colTenMon;
    @FXML private TableColumn<MenuItem, String> colGiaTien;

    private ObservableList<MenuItem> allItems;
    private ObservableList<MenuItem> filteredItems;

    @FXML
    public void initialize() {
        colTenMon.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colGiaTien.setCellValueFactory(cell -> 
            new SimpleStringProperty(String.format("%.0f", cell.getValue().getPrice()))
        );

        allItems = MenuStore.getItems();
        filteredItems = FXCollections.observableArrayList(allItems);

        tableKetQua.setItems(filteredItems);

        txtTimKiem.setOnAction(e -> handleTimKiem());
    }

    @FXML
    private void handleTimKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập từ khóa để tìm kiếm!");
            return;
        }

        filteredItems.setAll(allItems.filtered(item ->
            item.getName().toLowerCase().contains(keyword)
        ));

        if (filteredItems.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Kết quả", "Không tìm thấy món nào phù hợp.");
        }
    }

    @FXML
    private void handleLamMoi() {
        txtTimKiem.clear();
        filteredItems.setAll(allItems);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
