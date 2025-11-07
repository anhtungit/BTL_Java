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

public class MenuItemSearchController {

    @FXML
    private TextField txtSearchKeyword;
    @FXML
    private TableView<MenuItem> tableResult;
    @FXML
    private TableColumn<MenuItem, String> colItemName;
    @FXML
    private TableColumn<MenuItem, String> colCurrentPrice;

    private ObservableList<MenuItem> allItems;
    private ObservableList<MenuItem> filteredItems;

    MenuItemService menuItemService = new MenuItemServiceImpl();

    @FXML
    public void initialize() {
        // Load dữ liệu từ database
        // menuItemService.loadFromDatabase();

   colItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        colCurrentPrice.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));

        allItems = FXCollections.observableArrayList(menuItemService.getAllMenuItem());
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

        filteredItems.setAll(allItems.filtered(item -> item.getItemName().toLowerCase().contains(keyword)));

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
