package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.openjfx.Models.MenuItem;
import org.openjfx.Models.Order;
import org.openjfx.Models.OrderItem;
import org.openjfx.Models.Table;
import org.openjfx.Stores.MenuStore;
import org.openjfx.Stores.TableStore;

public class SalesController implements Initializable {
    
    @FXML private GridPane tableGrid;
    @FXML private Button btnViewTable;
    @FXML private Button btnTransferTable;
    @FXML private Button btnCancelTable;
    @FXML private Button btnReserveTable;
    @FXML private Button btnSelectMenu;
    @FXML private Label lblSelectedTable;
    @FXML private Label lblTableStatus;
    @FXML private Label lblCustomerName;
    
    private TableStore tableStore;
    private Table selectedTable;
    private List<Table> selectedTables = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableStore = TableStore.getInstance();
        initializeTableGrid();
        updateTableInfo();
    }

    private void initializeTableGrid() {
        // Tạo grid 5x4 cho 20 bàn
        for (int i = 0; i < 20; i++) {
            int tableNumber = i + 1;
            Table table = tableStore.getTable(tableNumber);
            
            Button tableButton = createTableButton(table);
            
            // Tính toán vị trí trong grid (5 hàng, 4 cột)
            int row = i / 4;
            int col = i % 4;
            
            tableGrid.add(tableButton, col, row);
        }
    }

    private Button createTableButton(Table table) {
        Button button = new Button();
        button.setText("Bàn " + String.format("%02d", table.getTableNumber()));
        button.setPrefSize(120, 80);
        button.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        updateTableButtonStyle(button, table);
        
        button.setOnAction(e -> selectTable(table));
        
        return button;
    }

    private void updateTableButtonStyle(Button button, Table table) {
        String style = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; ";
        
        if (table.isEmpty()) {
            style += "-fx-background-color: #e0e0e0; -fx-border-color: #9e9e9e;";
        } else if (table.isReserved()) {
            style += "-fx-background-color: #fff3e0; -fx-border-color: #ff9800;";
        }
        
        button.setStyle(style);
    }

    @FXML
    private void selectTable(Table table) {
        selectedTable = table;
        selectedTables.clear();
        selectedTables.add(table);
        updateTableInfo();
        refreshTableGrid();
    }

    private void updateTableInfo() {
        if (selectedTable != null) {
            lblSelectedTable.setText("Bàn " + String.format("%02d", selectedTable.getTableNumber()));
            lblTableStatus.setText(getStatusText(selectedTable.getStatus()));
            lblCustomerName.setText(selectedTable.getCustomerName().isEmpty() ? "Chưa có" : selectedTable.getCustomerName());
        } else {
            lblSelectedTable.setText("Chưa chọn");
            lblTableStatus.setText("-");
            lblCustomerName.setText("-");
        }
    }

    private String getStatusText(String status) {
        switch (status) {
            case "empty": return "Trống";
            case "reserved": return "Đã đặt";
            default: return "Không xác định";
        }
    }

    private void refreshTableGrid() {
        tableGrid.getChildren().clear();
        initializeTableGrid();
    }

    @FXML
    private void viewTable() {
        if (selectedTable == null) {
            showAlert("Thông báo", "Vui lòng chọn bàn để xem thông tin!");
            return;
        }
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Xem thông tin bàn " + String.format("%02d", selectedTable.getTableNumber()));
        
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        
        Label orderedLabel = new Label("Các món đã gọi");
        orderedLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        TableView<OrderItem> orderTable = new TableView<>();
        orderTable.setPrefHeight(200);

        java.util.List<OrderItem> items = tableStore.getItemsForTable(selectedTable.getTableNumber());
        orderTable.setItems(FXCollections.observableArrayList(items));
        
        TableColumn<OrderItem, String> itemNameCol = new TableColumn<>("Tên món");
        itemNameCol.setCellValueFactory(cd -> cd.getValue().itemNameProperty());
        itemNameCol.setPrefWidth(220);
        
        TableColumn<OrderItem, Integer> qtyCol = new TableColumn<>("SL");
        qtyCol.setCellValueFactory(cd -> cd.getValue().quantityProperty().asObject());
        qtyCol.setPrefWidth(80);
        
    orderTable.getColumns().add(itemNameCol);
    orderTable.getColumns().add(qtyCol);
        
        
        Label reserveInfo = new Label();
        String customer = selectedTable.getCustomerName().isEmpty() ? "-" : selectedTable.getCustomerName();
        reserveInfo.setText("Đặt trước\n" + customer);
        
        ButtonType closeType = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeType);
        
        content.getChildren().addAll(orderedLabel, orderTable, reserveInfo);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    @FXML
    private void transferTable() {
        if (selectedTable == null || !selectedTable.isReserved()) {
            showAlert("Lỗi", "Vui lòng chọn bàn đã đặt để chuyển!");
            return;
        }
        
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Chuyển bàn " + String.format("%02d", selectedTable.getTableNumber()));
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        Label instructionLabel = new Label("Chọn bàn cần chuyển đến:");
        instructionLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        content.getChildren().add(instructionLabel);
        
        ChoiceBox<Integer> tableChoiceBox = new ChoiceBox<>();
        List<Integer> availableTables = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Table table = tableStore.getTable(i);
            if (table != null && table.isEmpty() && i != selectedTable.getTableNumber()) {
                availableTables.add(i);
            }
        }
        
        if (availableTables.isEmpty()) {
            showAlert("Lỗi", "Không có bàn trống để chuyển!");
            return;
        }
        
        tableChoiceBox.getItems().addAll(availableTables);
        tableChoiceBox.setValue(availableTables.get(0));
        content.getChildren().add(tableChoiceBox);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button transferButton = new Button("Chuyển");
        transferButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16;");
        transferButton.setOnAction(e -> {
            Integer selectedTableNumber = tableChoiceBox.getValue();
            if (selectedTableNumber != null) {
                tableStore.transferTable(selectedTable.getTableNumber(), selectedTableNumber);
                selectedTable = tableStore.getTable(selectedTableNumber);
            updateTableInfo();
            refreshTableGrid();
                dialog.close();
            showAlert("Thành công", "Đã chuyển bàn thành công!");
            }
        });
        
        Button cancelButton = new Button("Hủy");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8 16;");
        cancelButton.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(transferButton, cancelButton);
        content.getChildren().add(buttonBox);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setResizable(false);
        
        dialog.setOnCloseRequest(e -> {
        });
        
        dialog.showAndWait();
    }


    @FXML
    private void cancelTable() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để hủy!");
            return;
        }
        
        if (selectedTable.isEmpty()) {
            showAlert("Lỗi", "Bàn này đã trống!");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc muốn hủy bàn " + selectedTable.getTableNumber() + "?");
        alert.setContentText("Hành động này sẽ xóa đơn hàng và làm trống bàn.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Order order = tableStore.getOrderByTable(selectedTable.getTableNumber());
            if (order != null) {
                tableStore.removeOrder(order);
            }
            tableStore.setItemsForTable(selectedTable.getTableNumber(), new java.util.ArrayList<>());
            selectedTable.setStatus("empty");
            selectedTable.setCustomerName("");
            updateTableInfo();
            refreshTableGrid();
            showAlert("Thành công", "Đã hủy bàn thành công!");
        }
    }

    @FXML
    private void reserveTable() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để đặt!");
            return;
        }
        
        if (!selectedTable.isEmpty()) {
            showAlert("Lỗi", "Bàn này không trống!");
            return;
        }
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Đặt bàn " + String.format("%02d", selectedTable.getTableNumber()));
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        HBox customerBox = new HBox(10);
        customerBox.setAlignment(Pos.CENTER_LEFT);
        Label customerLabel = new Label("Khách hàng:");
        customerLabel.setPrefWidth(80);
        TextField customerField = new TextField();
        customerField.setPrefWidth(200);
        customerBox.getChildren().addAll(customerLabel, customerField);
        
        HBox phoneBox = new HBox(10);
        phoneBox.setAlignment(Pos.CENTER_LEFT);
        Label phoneLabel = new Label("SĐT:");
        phoneLabel.setPrefWidth(80);
        TextField phoneField = new TextField();
        phoneField.setPrefWidth(200);
        phoneBox.getChildren().addAll(phoneLabel, phoneField);
        
        HBox dateBox = new HBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        Label dateLabel = new Label("Ngày:");
        dateLabel.setPrefWidth(80);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setPrefWidth(200);
        dateBox.getChildren().addAll(dateLabel, datePicker);
        
        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        Label timeLabel = new Label("Giờ:");
        timeLabel.setPrefWidth(80);
        TextField timeField = new TextField();
        timeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.setPrefWidth(200);
        timeBox.getChildren().addAll(timeLabel, timeField);
        
        content.getChildren().addAll(customerBox, phoneBox, dateBox, timeBox);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button reserveButton = new Button("Đặt bàn");
        reserveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 8 16;");
        reserveButton.setOnAction(e -> {
            String customerName = customerField.getText().trim();
            String phone = phoneField.getText().trim();
            
        if (customerName.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên khách hàng!");
            return;
        }
        
            if (phone.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập số điện thoại!");
                return;
            }
            
        tableStore.reserveTable(selectedTable.getTableNumber(), customerName);
        updateTableInfo();
        refreshTableGrid();
            dialog.close();
            
            javafx.application.Platform.runLater(() -> {
        showAlert("Thành công", "Đã đặt bàn thành công!");
            });
        });
        
        Button cancelButton = new Button("Hủy");
        cancelButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-padding: 8 16;");
        cancelButton.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(reserveButton, cancelButton);
        content.getChildren().add(buttonBox);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setResizable(false);
        
        dialog.setOnCloseRequest(e -> {
        });
        
        dialog.showAndWait();
    }


    @FXML
    private void selectMenu() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để chọn thực đơn!");
            return;
        }
        
        if (!selectedTable.isReserved()) {
            showAlert("Lỗi", "Bàn này chưa được đặt!");
            return;
        }
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Chọn món bàn " + String.format("%02d", selectedTable.getTableNumber()));
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        Label tableLabel = new Label("Bàn " + String.format("%02d", selectedTable.getTableNumber()));
        tableLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        content.getChildren().add(tableLabel);
        
        ObservableList<MenuItem> menuItems = MenuStore.getItems();

        java.util.List<OrderItem> existingItems = tableStore.getItemsForTable(selectedTable.getTableNumber());
        if (existingItems != null && !existingItems.isEmpty()) {
            for (MenuItem mi : menuItems) {
                for (OrderItem oi : existingItems) {
                    if (oi.getItemName().equals(mi.getName())) {
                        mi.setSelected(oi.getQuantity() > 0);
                        mi.setQuantity(oi.getQuantity());
                        break;
                    }
                }
            }
        }
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab mainDishTab = new Tab("Món chính");
        TableView<MenuItem> mainDishTable = new TableView<>();
        setupMenuTable(mainDishTable, MenuStore.getMainDishes());
        mainDishTab.setContent(mainDishTable);
        
        Tab drinksTab = new Tab("Đồ uống");
        TableView<MenuItem> drinksTable = new TableView<>();
        setupMenuTable(drinksTable, MenuStore.getDrinks());
        drinksTab.setContent(drinksTable);
        
        Tab dessertsTab = new Tab("Tráng miệng");
        TableView<MenuItem> dessertsTable = new TableView<>();
        setupMenuTable(dessertsTable, MenuStore.getDesserts());
        dessertsTab.setContent(dessertsTable);
        
        tabPane.getTabs().addAll(mainDishTab, drinksTab, dessertsTab);
        tabPane.setPrefHeight(300);
        tabPane.setMaxHeight(300);
        tabPane.setMaxWidth(320);
        
        TableColumn<MenuItem, Boolean> selectColumn = new TableColumn<>("Chọn");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<MenuItem, Boolean>() {
                private CheckBox checkBox;
                private MenuItem currentMenuItem;
                
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        currentMenuItem = null;
                    } else {
                        if (checkBox == null) {
                            checkBox = new CheckBox();
                        }
                        
                        currentMenuItem = getTableView().getItems().get(getIndex());
                        
                        checkBox.setSelected(item);
                        
                        checkBox.setOnAction(null);
                        checkBox.setOnAction(e -> {
                            if (currentMenuItem != null) {
                                currentMenuItem.setSelected(checkBox.isSelected());
                                if (!checkBox.isSelected()) {
                                    currentMenuItem.setQuantity(0);
                                } else if (currentMenuItem.getQuantity() == 0) {
                                    currentMenuItem.setQuantity(1);
                                }
                                getTableView().refresh();
                            }
                        });
                        
                        setGraphic(checkBox);
                    }
                }
            };
        });
        
        TableColumn<MenuItem, String> nameColumn = new TableColumn<>("Tên món");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(150);
        
        TableColumn<MenuItem, Integer> quantityColumn = new TableColumn<>("SL");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setPrefWidth(60);
        quantityColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<MenuItem, Integer>() {
                private Spinner<Integer> spinner;
                private MenuItem currentMenuItem;
                
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        currentMenuItem = null;
                    } else {
                        if (spinner == null) {
                            spinner = new Spinner<>(0, 10, 0);
                            spinner.setPrefWidth(50);
                            spinner.setMaxWidth(50);
                        }
                        
                        currentMenuItem = getTableView().getItems().get(getIndex());
                        
                        spinner.getValueFactory().setValue(item);
                        spinner.valueProperty().removeListener(this::onSpinnerValueChanged);
                        spinner.valueProperty().addListener(this::onSpinnerValueChanged);
                        
                        setGraphic(spinner);
                    }
                }
                
                private void onSpinnerValueChanged(javafx.beans.Observable obs, Integer oldVal, Integer newVal) {
                    if (currentMenuItem != null) {
                        currentMenuItem.setQuantity(newVal);
                        if (newVal > 0) {
                            currentMenuItem.setSelected(true);
                        } else {
                            currentMenuItem.setSelected(false);
                        }
                        getTableView().refresh();
                    }
                }
            };
        });
        
        
        content.getChildren().add(tabPane);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveButton = new Button("Lưu");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16;");
        saveButton.setOnAction(e -> {
            java.util.List<OrderItem> itemsToSave = new java.util.ArrayList<>();
            StringBuilder selectedItems = new StringBuilder("Đã chọn:\n");
            double totalAmount = 0;

            for (Tab tab : tabPane.getTabs()) {
                TableView<MenuItem> currentTable = (TableView<MenuItem>) tab.getContent();
                for (MenuItem item : currentTable.getItems()) {
                    if (item.isSelected() && item.getQuantity() > 0) {
                        itemsToSave.add(new OrderItem(item.getName(), item.getPrice(), item.getQuantity()));
                        selectedItems.append("- ").append(item.getName())
                                   .append(" x").append(item.getQuantity())
                                   .append(" = ").append(String.format("%,d VNĐ", (long)item.getTotalPrice()))
                                   .append("\n");
                        totalAmount += item.getTotalPrice();
                    }
                }
            }

            if (itemsToSave.isEmpty()) {
                showAlert("Thông báo", "Vui lòng chọn ít nhất một món!");
                return;
            }

            selectedItems.append("\nTổng tiền: ").append(String.format("%,d VNĐ", (long)totalAmount));
            tableStore.setItemsForTable(selectedTable.getTableNumber(), itemsToSave);
            
            dialog.close();
            showAlert("Thành công", selectedItems.toString());
        });
        
        Button cancelButton = new Button("Hủy");
        cancelButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-padding: 8 16;");
        cancelButton.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        content.getChildren().add(buttonBox);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setResizable(false);
        
        dialog.setOnCloseRequest(e -> {
        });
        
        dialog.showAndWait();
    }


    @FXML
    private void payment() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để thanh toán!");
            return;
        }
        
        if (!selectedTable.isReserved()) {
            showAlert("Lỗi", "Bàn này chưa được đặt!");
            return;
        }

        List<OrderItem> items = tableStore.getItemsForTable(selectedTable.getTableNumber());
        if (items == null || items.isEmpty()) {
            showAlert("Lỗi", "Không có món ăn nào để thanh toán!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/payment_dialog.fxml"));
            Parent root = loader.load();
            
            PaymentDialogController dialogController = loader.getController();
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Thanh toán - Bàn " + selectedTable.getTableNumber());
            dialog.setDialogPane((DialogPane) root);
            
            dialogController.setTable(selectedTable);
            dialog.showAndWait();
            
            if (dialogController.isResetTableSelected()) {
                Order order = tableStore.getOrderByTable(selectedTable.getTableNumber());
                if (order != null) {
                    tableStore.removeOrder(order);
                }
                
                selectedTable.setStatus("empty");
                selectedTable.setCustomerName("");
                tableStore.setItemsForTable(selectedTable.getTableNumber(), new ArrayList<>());
                
                updateTableInfo();
                refreshTableGrid();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở cửa sổ thanh toán!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupMenuTable(TableView<MenuItem> tableView, ObservableList<MenuItem> items) {
        tableView.setItems(items);
        tableView.setPrefHeight(300);
        tableView.setMaxHeight(300);
        tableView.setMaxWidth(320);

        TableColumn<MenuItem, Boolean> selectColumn = new TableColumn<>("Chọn");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<MenuItem, Boolean>() {
                private CheckBox checkBox;
                private MenuItem currentMenuItem;
                
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        currentMenuItem = null;
                    } else {
                        if (checkBox == null) {
                            checkBox = new CheckBox();
                        }
                        
                        currentMenuItem = getTableView().getItems().get(getIndex());
                        checkBox.setSelected(item);
                        
                        checkBox.setOnAction(null);
                        checkBox.setOnAction(e -> {
                            if (currentMenuItem != null) {
                                currentMenuItem.setSelected(checkBox.isSelected());
                                if (!checkBox.isSelected()) {
                                    currentMenuItem.setQuantity(0);
                                } else if (currentMenuItem.getQuantity() == 0) {
                                    currentMenuItem.setQuantity(1);
                                }
                                getTableView().refresh();
                            }
                        });
                        
                        setGraphic(checkBox);
                    }
                }
            };
        });
        
        TableColumn<MenuItem, String> nameColumn = new TableColumn<>("Tên món");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(150);
        
        TableColumn<MenuItem, String> priceColumn = new TableColumn<>("Giá");
        priceColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.format("%,d VNĐ", (long)cellData.getValue().getPrice())
            )
        );
        priceColumn.setPrefWidth(100);
        
        TableColumn<MenuItem, Integer> quantityColumn = new TableColumn<>("SL");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setPrefWidth(60);
        quantityColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<MenuItem, Integer>() {
                private Spinner<Integer> spinner;
                private MenuItem currentMenuItem;
                
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        currentMenuItem = null;
                    } else {
                        if (spinner == null) {
                            spinner = new Spinner<>(0, 10, 0);
                            spinner.setPrefWidth(50);
                            spinner.setMaxWidth(50);
                        }
                        
                        currentMenuItem = getTableView().getItems().get(getIndex());
                        spinner.getValueFactory().setValue(item);
                        
                        spinner.valueProperty().removeListener(this::onSpinnerValueChanged);
                        spinner.valueProperty().addListener(this::onSpinnerValueChanged);
                        
                        setGraphic(spinner);
                    }
                }
                
                private void onSpinnerValueChanged(javafx.beans.Observable obs, Integer oldVal, Integer newVal) {
                    if (currentMenuItem != null) {
                        currentMenuItem.setQuantity(newVal);
                        if (newVal > 0) {
                            currentMenuItem.setSelected(true);
                        } else {
                            currentMenuItem.setSelected(false);
                        }
                        getTableView().refresh();
                    }
                }
            };
        });
        
    tableView.getColumns().add(selectColumn);
    tableView.getColumns().add(nameColumn);
    tableView.getColumns().add(priceColumn);
    tableView.getColumns().add(quantityColumn);
    }
}
