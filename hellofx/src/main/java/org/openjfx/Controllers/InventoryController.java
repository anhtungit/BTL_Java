package org.openjfx.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.openjfx.App;
import org.openjfx.entity.ExportNote;
import org.openjfx.entity.ImportNote;
import org.openjfx.entity.InventoryItem;
import org.openjfx.service.ExportNoteService;
import org.openjfx.service.ImportNoteService;
import org.openjfx.service.InventoryItemService;
import org.openjfx.service.UnitService;
import org.openjfx.service.impl.ExportNoteServiceImpl;
import org.openjfx.service.impl.ImportNoteServiceImpl;
import org.openjfx.service.impl.InventoryItemServiceImpl;
import org.openjfx.service.impl.UnitServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InventoryController {
    @FXML private TextField searchField;
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, Date> importDateColumn;
    @FXML private TableColumn<InventoryItem, Date> exportDateColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML private TableColumn<InventoryItem, String> unitColumn;
    @FXML private TableColumn<InventoryItem, Integer> priceColumn;
    @FXML private TableColumn<InventoryItem, Integer> totalColumn;

    private ObservableList<InventoryItem> inventoryItems;
    private FilteredList<InventoryItem> filteredItems;

    InventoryItemService inventoryItemService = new InventoryItemServiceImpl();
    UnitService unitService = new UnitServiceImpl();
    ImportNoteService importNoteService = new ImportNoteServiceImpl();
    ExportNoteService exportNoteService = new ExportNoteServiceImpl();

    @FXML
    public void initialize() {
        // Get inventory items from store
        inventoryItems = FXCollections.observableArrayList(inventoryItemService.getAllInventoryItem());
        filteredItems = new FilteredList<>(inventoryItems);

        // Setup table columns
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getItemName()));
        importDateColumn.setCellValueFactory(c -> new SimpleObjectProperty<Date>(importNoteService.getImportNoteByInventoryID(c.getValue().getInventoryItemID()).getImportDate()));
        exportDateColumn.setCellValueFactory(c -> new SimpleObjectProperty<Date>(exportNoteService.getExportNoteByInventoryID(c.getValue().getInventoryItemID()).getExportDate()));
        quantityColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStockQuantity()).asObject());
        unitColumn.setCellValueFactory(c -> new SimpleStringProperty(unitService.getUnitByUnitID(c.getValue().getUnitID()).getUnitName()));
        priceColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getUnitPrice()).asObject());
        totalColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getUnitPrice() * c.getValue().getStockQuantity()).asObject());

        priceColumn.setCellFactory(column -> new TableCell<InventoryItem, Integer>() {
            @Override
            protected void updateItem(Integer price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d VNĐ", price));
                    setAlignment(Pos.BASELINE_RIGHT);
                }
            }
        });

        totalColumn.setCellFactory(column -> new TableCell<InventoryItem, Integer>() {
            @Override
            protected void updateItem(Integer total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d VNĐ", total.longValue()));
                    setAlignment(Pos.BASELINE_RIGHT);
                }
            }
        });

        inventoryTable.setItems(filteredItems);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getItemName().toLowerCase().contains(lowerCaseFilter);
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

        List<String> unitList = unitService.getAllUnit().stream().map(u -> u.getUnitName()).toList();
        unitCombo.setItems(FXCollections.observableArrayList(unitList));
        unitCombo.setValue(unitList.get(0));

        // Disable OK button by default
        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            try {
                if (nameField.getText().isEmpty()) {
                    showAlert("Lỗi", "Vui lòng nhập tên hàng");
                    event.consume();
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng phải lớn hơn 0");
                    event.consume();
                    return;
                }
                int price = Integer.parseInt(priceField.getText());
                if (price <= 0) {
                    showAlert("Lỗi", "Đơn giá phải lớn hơn 0");
                    event.consume();
                    return;
                }
                if (importDatePicker.getValue() == null) {
                    showAlert("Lỗi", "Vui lòng chọn ngày nhập");
                    event.consume();
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Vui lòng nhập số hợp lệ");
                event.consume();
            }
        });

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                int unitID = unitService.getUnitByUnitName(unitCombo.getValue()).getUnitId();
                int price = Integer.parseInt(priceField.getText());

                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setItemName(name);
                inventoryItem.setStockQuantity(quantity);
                inventoryItem.setUnitID(unitID);
                inventoryItem.setUnitPrice(price);

                return inventoryItem;
            }
            return null;
        });

        Optional<InventoryItem> result = dialog.showAndWait();
        result.ifPresent(item -> {
            int inventoryID = inventoryItemService.create(item);
            ImportNote importNote = new ImportNote();
            importNote.setInventoryItemID(inventoryID);
            importNote.setQuantity(item.getStockQuantity());
            importNote.setImportDate(Date.valueOf(importDatePicker.getValue()));
            importNote.setTotalAmount(item.getStockQuantity() * item.getUnitPrice());
            importNote.setEmployeeID(App.getEmployeeLogin().getEmployeeID());

            importNoteService.create(importNote);
            initialize();
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

        // Add event filter to validate before closing
        Button exportButton = (Button) dialog.getDialogPane().lookupButton(exportButtonType);
        exportButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            try {
                if (quantityField.getText().isEmpty()) {
                    showAlert("Lỗi", "Vui lòng nhập số lượng xuất");
                    event.consume();
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng phải lớn hơn 0");
                    event.consume();
                    return;
                }
                if (quantity > selectedItem.getStockQuantity()) {
                    showAlert("Lỗi", "Số lượng xuất vượt quá số lượng trong kho");
                    event.consume();
                    return;
                }
                if (exportDatePicker.getValue() == null) {
                    showAlert("Lỗi", "Vui lòng chọn ngày xuất");
                    event.consume();
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Vui lòng nhập số hợp lệ");
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == exportButtonType) {
                int quantity = Integer.parseInt(quantityField.getText());
                LocalDate exportDate = exportDatePicker.getValue();

                ExportNote exportNote = new ExportNote();
                exportNote.setInventoryItemID(selectedItem.getInventoryItemID());
                exportNote.setExportDate(Date.valueOf(exportDate));
                exportNote.setTotalExportAmount(quantity * selectedItem.getUnitPrice());
                exportNote.setEmployeeID(App.getEmployeeLogin().getEmployeeID());
                exportNote.setQuantity(quantity);

                exportNoteService.create(exportNote);

                selectedItem.setStockQuantity(selectedItem.getStockQuantity() - quantity);
                inventoryItemService.save(selectedItem);
                return quantity;
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

        TextField nameField = new TextField(selectedItem.getItemName());
        TextField quantityField = new TextField(String.valueOf(selectedItem.getStockQuantity()));
        ComboBox<String> unitCombo = new ComboBox<>();
        TextField priceField = new TextField(String.valueOf(selectedItem.getUnitPrice()));
        DatePicker importDatePicker = new DatePicker(importNoteService.getImportNoteByInventoryID(selectedItem.getInventoryItemID()).getImportDate().toLocalDate());

        unitCombo.setItems(FXCollections.observableArrayList(unitService.getAllUnit().stream().map(u -> u.getUnitName()).toList()));
        unitCombo.setValue(unitService.getUnitByUnitID(selectedItem.getUnitID()).getUnitName());
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

        // Add event filter to validate before closing
        Button editButton = (Button) dialog.getDialogPane().lookupButton(editButtonType);
        editButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            try {
                if (nameField.getText().isEmpty()) {
                    showAlert("Lỗi", "Vui lòng nhập tên hàng");
                    event.consume();
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng phải lớn hơn 0");
                    event.consume();
                    return;
                }
                int price = Integer.parseInt(priceField.getText());
                if (price <= 0) {
                    showAlert("Lỗi", "Đơn giá phải lớn hơn 0");
                    event.consume();
                    return;
                }
                if (importDatePicker.getValue() == null) {
                    showAlert("Lỗi", "Vui lòng chọn ngày nhập");
                    event.consume();
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Vui lòng nhập số hợp lệ");
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                selectedItem.setItemName(nameField.getText());
                selectedItem.setStockQuantity(Integer.parseInt(quantityField.getText()));
                selectedItem.setUnitID(unitService.getUnitByUnitName(unitCombo.getValue()).getUnitId());
                selectedItem.setUnitPrice(Integer.parseInt(priceField.getText()));

                ImportNote importNote = importNoteService.getImportNoteByInventoryID(selectedItem.getInventoryItemID());
                importNote.setImportDate(Date.valueOf(importDatePicker.getValue()));

                inventoryItemService.save(selectedItem);
                importNoteService.save(importNote);
                return selectedItem;
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
            inventoryItemService.delete(selectedItem);
            initialize();
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