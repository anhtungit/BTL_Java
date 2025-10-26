package org.openjfx.Controllers;

import org.openjfx.Models.Ingredient;
import org.openjfx.Models.MenuItem;
import org.openjfx.Stores.MenuStore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

public class MenuItemAddController {

    @FXML private TextField txtItemName;
    @FXML private TextField txtCurrentPrice;
    @FXML private TableView<Ingredient> tableRecipe;
    @FXML private TableColumn<Ingredient, String> colInventoryItemName;
    @FXML private TableColumn<Ingredient, String> colAmount;
    @FXML private TableColumn<Ingredient, String> colUnitName;

    private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colInventoryItemName.setCellValueFactory(cell -> cell.getValue().inventoryItemNameProperty());
        colAmount.setCellValueFactory(cell -> cell.getValue().amountProperty());
        colUnitName.setCellValueFactory(cell -> cell.getValue().unitNameProperty());
        tableRecipe.setItems(ingredients);

        tableRecipe.setEditable(true);

        colInventoryItemName.setCellFactory(TextFieldTableCell.forTableColumn());
        colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
        colUnitName.setCellFactory(TextFieldTableCell.forTableColumn());

        colInventoryItemName.setOnEditCommit(event -> event.getRowValue().setInventoryItemName(event.getNewValue()));
        colAmount.setOnEditCommit(event -> event.getRowValue().setAmount(event.getNewValue()));
        colUnitName.setOnEditCommit(event -> event.getRowValue().setUnitName(event.getNewValue()));

        for (int i = 0; i < 3; i++) {
            ingredients.add(new Ingredient("", "", ""));
        }
    }

    @FXML
    private void handleAddMenuItem() {
        String name = txtItemName.getText().trim();
        String priceText = txtCurrentPrice.getText().trim();

        if (name.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên món!");
            return;
        }

        if (priceText.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập giá tiền!");
            return;
        }

        try {
            String cleanPriceText = priceText.replaceAll("[^0-9]", "");
            double price = Double.parseDouble(cleanPriceText);

            if (price <= 0) {
                showAlert("Lỗi", "Gía tiền phải lớn hơn 0!");
                return;
            }

            MenuStore.addItem(new MenuItem(name, price));

            txtItemName.clear();
            txtCurrentPrice.clear();
            ingredients.clear();
            for (int i = 0; i < 3; i++) {
                ingredients.add(new Ingredient("", "", ""));
            }

            showAlert("Thành công!", "Đã thêm món \"" + name + "\" vào thực đơn!");
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Gía tiền không hợp lệ! Vui lòng chỉ nhập số.");
        }
    }

    @FXML
    private void handleCancel() {
        txtItemName.clear();
        txtCurrentPrice.clear();
        ingredients.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
