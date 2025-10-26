package org.openjfx.Controllers;

import org.openjfx.Models.MenuItem;
import org.openjfx.Stores.MenuStore;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MenuItemSearchController {

    @FXML private TextField txtSearchKeyword;
    @FXML private TableView<MenuItem> tableResult;
    @FXML private TableColumn<MenuItem, String> colItemName;
    @FXML private TableColumn<MenuItem, String> colCurrentPrice;

    private ObservableList<MenuItem> allItems;
    private ObservableList<MenuItem> filteredItems;

    @FXML
    public void initialize() {
        colItemName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colCurrentPrice.setCellValueFactory(cell -> 
            new SimpleStringProperty(String.format("%.0f", cell.getValue().getPrice()))
        );

        allItems = MenuStore.getItems();
        filteredItems = FXCollections.observableArrayList(allItems);

        tableResult.setItems(filteredItems);

        txtSearchKeyword.setOnAction(e -> handleSearch());
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearchKeyword.getText().trim().toLowerCase();

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
    private void handleRefresh() {
        txtSearchKeyword.clear();
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
