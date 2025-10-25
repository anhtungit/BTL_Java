package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.time.LocalDate;
import java.util.Optional;

public class InventoryController {
    @FXML private TextField searchField;
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, LocalDate> importDateColumn;
    @FXML private TableColumn<InventoryItem, LocalDate> exportDateColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML private TableColumn<InventoryItem, String> unitColumn;
    @FXML private TableColumn<InventoryItem, Double> priceColumn;
    @FXML private TableColumn<InventoryItem, Double> totalColumn;

    private ObservableList<InventoryItem> inventoryItems;
    private FilteredList<InventoryItem> filteredItems;

    @FXML
    public void initialize() {
        inventoryItems = FXCollections.observableArrayList();
        filteredItems = new FilteredList<>(inventoryItems);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        exportDateColumn.setCellValueFactory(new PropertyValueFactory<>("exportDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        inventoryTable.setItems(filteredItems);

        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    private void handleSearch() {
        // Search is already handled by FilteredList listener
    }

    @FXML
    private void showAddDialog() {
        Dialog<InventoryItem> dialog = new Dialog<>();
        dialog.setTitle("Nhập hàng hóa");
        dialog.setHeaderText(null);

        // Set the button types
        ButtonType addButtonType = new ButtonType("Đồng ý", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField quantityField = new TextField();
        ComboBox<String> unitCombo = new ComboBox<>();
        TextField priceField = new TextField();
        DatePicker importDatePicker = new DatePicker();

        grid.add(new Label("Tên hàng:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Số lượng:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Đơn vị:"), 0, 2);
        grid.add(unitCombo, 1, 2);
        grid.add(new Label("Đơn giá:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Ngày nhập:"), 0, 4);
        grid.add(importDatePicker, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    String unit = unitCombo.getValue();
                    double price = Double.parseDouble(priceField.getText());
                    LocalDate importDate = importDatePicker.getValue();

                    return new InventoryItem(name, quantity, unit, price, importDate);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input values");
                    return null;
                }
            }
            return null;
        });

        Optional<InventoryItem> result = dialog.showAndWait();
        result.ifPresent(item -> {
            inventoryItems.add(item);
        });
    }

    @FXML
    private void showExportDialog() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to export");
            return;
        }

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Xuất hàng hóa");
        dialog.setHeaderText(null);

        ButtonType exportButtonType = new ButtonType("Đồng ý", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(exportButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField quantityField = new TextField();
        DatePicker exportDatePicker = new DatePicker();

        grid.add(new Label("Số lượng xuất:"), 0, 0);
        grid.add(quantityField, 1, 0);
        grid.add(new Label("Ngày xuất:"), 0, 1);
        grid.add(exportDatePicker, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == exportButtonType) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    LocalDate exportDate = exportDatePicker.getValue();

                    if (quantity > selectedItem.getQuantity()) {
                        showAlert("Error", "Số lượng xuất vượt quá số lượng trong kho");
                        return null;
                    }

                    selectedItem.setQuantity(selectedItem.getQuantity() - quantity);
                    selectedItem.setExportDate(exportDate);
                    return quantity;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid quantity");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
        inventoryTable.refresh();
    }

    @FXML
    private void showEditDialog() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to edit");
            return;
        }

        Dialog<InventoryItem> dialog = new Dialog<>();
        dialog.setTitle("Chỉnh sửa hàng hóa");
        dialog.setHeaderText(null);

        ButtonType editButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(selectedItem.getName());
        TextField quantityField = new TextField(String.valueOf(selectedItem.getQuantity()));
        TextField unitField = new TextField(selectedItem.getUnit());
        TextField priceField = new TextField(String.valueOf(selectedItem.getPrice()));
        DatePicker importDatePicker = new DatePicker(selectedItem.getImportDate());

        grid.add(new Label("Tên hàng:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Số lượng:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Đơn vị:"), 0, 2);
        grid.add(unitField, 1, 2);
        grid.add(new Label("Đơn giá:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Ngày nhập:"), 0, 4);
        grid.add(importDatePicker, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                try {
                    selectedItem.setName(nameField.getText());
                    selectedItem.setQuantity(Integer.parseInt(quantityField.getText()));
                    selectedItem.setUnit(unitField.getText());
                    selectedItem.setPrice(Double.parseDouble(priceField.getText()));
                    selectedItem.setImportDate(importDatePicker.getValue());
                    return selectedItem;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input values");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
        inventoryTable.refresh();
    }

    @FXML
    private void handleDelete() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa mặt hàng này?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            inventoryItems.remove(selectedItem);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}