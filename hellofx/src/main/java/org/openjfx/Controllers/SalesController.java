package org.openjfx.Controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.converter.IntegerStringConverter;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.openjfx.App;
import org.openjfx.entity.BookingDetail;
import org.openjfx.entity.Invoice;
import org.openjfx.entity.InvoiceDetail;
import org.openjfx.entity.Table;
import org.openjfx.entity.MenuItem;
import org.openjfx.service.*;
import org.openjfx.service.impl.*;

public class SalesController implements Initializable {

    @FXML
    private GridPane tableGrid;
    @FXML
    private Button btnViewTable;
    @FXML
    private Button btnTransferTable;
    @FXML
    private Button btnCancelTable;
    @FXML
    private Button btnReserveTable;
    @FXML
    private Button btnSelectMenu;
    @FXML
    private Label lblSelectedTable;
    @FXML
    private Label lblTableStatus;
    @FXML
    private Label lblCustomerName;

    List<Table> tableList;

    private Table selectedTable;
    private BookingDetail bookingDetailOfSelectedTable;
    private Invoice invoiceOfSelectedTable;
    private List<InvoiceDetail> listOfInvoiceDetailOfSelectedTable;

    TableService tableService = new TableServiceImpl();
    BookingDetailService bookingDetailService = new BookingDetailServiceImpl();
    InvoiceService invoiceService = new InvoiceServiceImpl();
    InvoiceDetailService invoiceDetailService = new InvoiceDetailServiceImpl();
    MenuItemService menuItemService = new MenuItemServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableList = tableService.getAllTables();
        initializeTableGrid();
        updateTableInfo();
    }

    private void initializeTableGrid() {
        // Tạo grid 5x4 cho 20 bàn
        for (int i = 0; i < tableList.size(); i++) {
            Table table = tableList.get(i);

            Button tableButton = createTableButton(table);

            // Tính toán vị trí trong grid (5 hàng, 4 cột)
            int row = i / 4;
            int col = i % 4;

            tableGrid.add(tableButton, col, row);
        }
    }

    private Button createTableButton(Table table) {
        Button button = new Button();
        button.setText(table.getTableName());
        button.setPrefSize(120, 80);
        button.setFont(Font.font("System", FontWeight.BOLD, 12));

        updateTableButtonStyle(button, table);

        button.setOnAction(e -> selectTable(table));

        return button;
    }

    private void updateTableButtonStyle(Button button, Table table) {
        String style = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; ";

        if ("Trống".equals(table.getStatus())) {
            style += "-fx-background-color: #e0e0e0; -fx-border-color: #9e9e9e;";
        } else if ("Đã đặt".equals(table.getStatus())) {
            style += "-fx-background-color: #fff3e0; -fx-border-color: #ff9800;";
        }

        button.setStyle(style);
    }

    @FXML
    private void selectTable(Table table) {
        selectedTable = table;
        updateTableInfo();
        refreshTableGrid();
    }

    private void updateTableInfo() {
        if (selectedTable != null) {
            if (!"Trống".equals(selectedTable.getStatus())) {
                bookingDetailOfSelectedTable = bookingDetailService
                        .getBookingDetailNewlestByTableID(selectedTable.getTableID());
                invoiceOfSelectedTable = invoiceService
                        .getInvoiceByInvoiceID(bookingDetailOfSelectedTable.getInvoiceID());
                listOfInvoiceDetailOfSelectedTable = invoiceDetailService
                        .getInvoiceDetailByInvoiceID(invoiceOfSelectedTable.getInvoiceID());

                lblSelectedTable.setText(selectedTable.getTableName());
                lblTableStatus.setText(selectedTable.getStatus());
                lblCustomerName.setText(bookingDetailOfSelectedTable.getCustomerName());
            } else {
                bookingDetailOfSelectedTable = new BookingDetail();
                invoiceOfSelectedTable = new Invoice();
                listOfInvoiceDetailOfSelectedTable = new ArrayList<>();

                lblSelectedTable.setText("Chưa có");
                lblTableStatus.setText(selectedTable.getStatus());
                lblCustomerName.setText(bookingDetailOfSelectedTable.getCustomerName());
            }

        } else {
            lblSelectedTable.setText("Chưa chọn");
            lblTableStatus.setText("-");
            lblCustomerName.setText("-");
        }
    }

    private void refreshTableGrid() {
        tableGrid.getChildren().clear();
        tableList.clear();
        tableList = tableService.getAllTables();
        initializeTableGrid();
    }

    @FXML
    private void viewTable() { // xong
        if (selectedTable == null) {
            showAlert("Thông báo", "Vui lòng chọn bàn để xem thông tin!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Xem thông tin bàn " + selectedTable.getTableName());

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));

        Label orderedLabel = new Label("Các món đã gọi");
        orderedLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        TableView<InvoiceDetail> orderTable = new TableView<>();
        orderTable.setPrefHeight(200);
        orderTable.setItems(FXCollections.observableArrayList(listOfInvoiceDetailOfSelectedTable)
                .filtered(c -> c.getQuantity() > 0));

        TableColumn<InvoiceDetail, String> itemNameCol = new TableColumn<>("Tên món");
        itemNameCol.setCellValueFactory(cd -> new SimpleStringProperty(
                menuItemService.getMenuItemByMenuItemID(cd.getValue().getMenuItemID()).getItemName()));
        itemNameCol.setPrefWidth(220);

        TableColumn<InvoiceDetail, Integer> qtyCol = new TableColumn<>("SL");
        qtyCol.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getQuantity()).asObject());
        qtyCol.setPrefWidth(80);

        orderTable.getColumns().add(itemNameCol);
        orderTable.getColumns().add(qtyCol);

        lblSelectedTable.setText(selectedTable.getTableName());
        lblTableStatus.setText(selectedTable.getStatus());
        lblCustomerName.setText(bookingDetailOfSelectedTable.getCustomerName());
        Label reserveInfo = new Label();
        String customer = ("Trống".equals(selectedTable.getStatus()) ? "-"
                : bookingDetailOfSelectedTable.getCustomerName());
        reserveInfo.setText("Đặt trước\n" + customer);

        ButtonType closeType = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeType);

        content.getChildren().addAll(orderedLabel, orderTable, reserveInfo);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    @FXML
    private void transferTable() { // xong
        if (selectedTable == null || !"Đã đặt".equals(selectedTable.getStatus())) {
            showAlert("Lỗi", "Vui lòng chọn bàn đã đặt để chuyển!");
            return;
        }

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Chuyển bàn " + selectedTable.getTableName());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        Label instructionLabel = new Label("Chọn bàn cần chuyển đến:");
        instructionLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        content.getChildren().add(instructionLabel);

        ChoiceBox<String> tableChoiceBox = new ChoiceBox<>();
        List<Integer> availableTables = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Table table = tableList.get(i);
            if (table != null && "Trống".equals(table.getStatus()) && i + 1 != selectedTable.getTableID()) {
                availableTables.add(i + 1);
            }
        }

        if (availableTables.isEmpty()) {
            showAlert("Lỗi", "Không có bàn trống để chuyển!");
            return;
        }

        tableChoiceBox.getItems()
                .addAll(availableTables.stream().map(a -> tableService.getTableByTableID(a).getTableName()).toList());
        tableChoiceBox.setValue(tableService.getTableByTableID(availableTables.get(0)).getTableName());
        content.getChildren().add(tableChoiceBox);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button transferButton = new Button("Chuyển");
        transferButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16;");
        transferButton.setOnAction(e -> {
            Table destTable = tableService.getTableByTableName(tableChoiceBox.getValue());
            if (destTable != null) {
                bookingDetailService.changeTableInBookingDetail(selectedTable, destTable);
                tableService.changeStatusTable(selectedTable);
                tableService.changeStatusTable(destTable);
                selectedTable = destTable;
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
    private void cancelTable() { // gần xong
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để hủy!");
            return;
        }

        if ("Trống".equals(selectedTable.getStatus())) {
            showAlert("Lỗi", "Bàn này đã trống!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc muốn hủy bàn " + selectedTable.getTableName() + "?");
        alert.setContentText("Hành động này sẽ xóa đơn hàng và làm trống bàn.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            invoiceDetailService.deleteAll(invoiceOfSelectedTable);
            bookingDetailService.delete(bookingDetailOfSelectedTable);
            invoiceService.delete(invoiceOfSelectedTable);

            tableService.changeStatusTable(selectedTable);
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

        if ("Đã đặt".equals(selectedTable.getStatus())) {
            showAlert("Lỗi", "Bàn này không trống!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Đặt bàn " + selectedTable.getTableName());

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
        
        // Add phone number validation - only numbers, max 10 digits
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 10) {
                phoneField.setText(newValue.substring(0, 10));
            }
        });
        
        phoneBox.getChildren().addAll(phoneLabel, phoneField);

        HBox dateBox = new HBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        Label dateLabel = new Label("Ngày:");
        dateLabel.setPrefWidth(80);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setPrefWidth(200);
        datePicker.setEditable(false);
        dateBox.getChildren().addAll(dateLabel, datePicker);

        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        Label timeLabel = new Label("Giờ:");
        timeLabel.setPrefWidth(80);
        TextField timeField = new TextField();
        timeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.setPrefWidth(200);
        
        // Add time input validation and auto-formatting with hour validation
        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                timeField.setText("");
                return;
            }
            
            // Remove all non-digit characters
            String digitsOnly = newValue.replaceAll("[^\\d]", "");
            
            // Limit to maximum 4 digits (HHMM)
            if (digitsOnly.length() > 4) {
                digitsOnly = digitsOnly.substring(0, 4);
            }
            
            // Auto-format as HH:mm with validation
            String formatted;
            if (digitsOnly.isEmpty()) {
                formatted = "";
            } else if (digitsOnly.length() == 1) {
                formatted = digitsOnly;
            } else if (digitsOnly.length() == 2) {
                int hour = Integer.parseInt(digitsOnly);
                if (hour > 23) {
                    // Keep only valid hours
                    formatted = digitsOnly.substring(0, 1);
                } else {
                    formatted = digitsOnly;
                }
            } else if (digitsOnly.length() == 3) {
                int hour = Integer.parseInt(digitsOnly.substring(0, 2));
                if (hour > 23) {
                    formatted = digitsOnly.substring(0, 1);
                } else {
                    formatted = digitsOnly.substring(0, 2) + ":" + digitsOnly.substring(2);
                }
            } else { // length == 4
                int hour = Integer.parseInt(digitsOnly.substring(0, 2));
                int minute = Integer.parseInt(digitsOnly.substring(2, 4));
                if (hour > 23) {
                    formatted = digitsOnly.substring(0, 1) + ":" + digitsOnly.substring(1, 3);
                } else if (minute > 59) {
                    formatted = digitsOnly.substring(0, 2) + ":" + digitsOnly.substring(2, 3);
                } else {
                    formatted = digitsOnly.substring(0, 2) + ":" + digitsOnly.substring(2);
                }
            }
            
            timeField.setText(formatted);
        });
        
        timeBox.getChildren().addAll(timeLabel, timeField);

        content.getChildren().addAll(customerBox, phoneBox, dateBox, timeBox);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button reserveButton = new Button("Đặt bàn");
        reserveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 8 16;");
        reserveButton.setOnAction(e -> {
            String customerName = customerField.getText().trim();
            String phone = phoneField.getText().trim();
            String time = timeField.getText().trim();

            if (customerName.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập tên khách hàng!");
                return;
            }

            if (phone.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập số điện thoại!");
                return;
            }

            if (phone.length() != 10) {
                showAlert("Lỗi", "Số điện thoại phải đúng 10 số!");
                return;
            }

            if (time.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập giờ!");
                return;
            }

            if (!time.matches("\\d{2}:\\d{2}")) {
                showAlert("Lỗi", "Giờ phải có định dạng HH:mm!");
                return;
            }

            int newInvoiceId = invoiceService.create();
            LocalDate date = datePicker.getValue();
            BookingDetail b = new BookingDetail();
            b.setTableID(selectedTable.getTableID());
            b.setInvoiceID(newInvoiceId);
            b.setEmployeeID(App.getEmployeeLogin().getEmployeeID());
            b.setCustomerName(customerName);
            b.setCustomerPhone(phone);
            b.setBookingDateTime(Date.valueOf(date));

            bookingDetailService.create(b);
            tableService.changeStatusTable(selectedTable);

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

        if ("Trống".equals(selectedTable.getStatus())) {
            showAlert("Lỗi", "Bàn này chưa được đặt!");
            return;
        }

        // Tạo popup chọn thực đơn
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Chọn món bàn " + selectedTable.getTableName());

        // Tạo layout cho popup
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Label "Bàn XX"
        Label tableLabel = new Label("Bàn " + selectedTable.getTableName());
        tableLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        content.getChildren().add(tableLabel);

        // Tạo danh sách món ăn và danh sách tạm thời cho items được chọn
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(menuItemService.getAllMenuItem());
        // List<MenuItem> tempSelectedItems = new ArrayList<>();
        // Sử dụng Map để lưu món ăn và số lượng (ví dụ: ID món ăn -> Số lượng)
        Map<Integer, Integer> tempSelectedQuantities = new HashMap<>();
        for (MenuItem item : menuItems) {
            tempSelectedQuantities.put(item.getMenuItemId(), 0);
        }
        // ... (Các phần khởi tạo khác giữ nguyên)

        // Tạo TableView
        TableView<MenuItem> tableView = new TableView<>();
        tableView.setItems(menuItems);
        tableView.setPrefHeight(300);
        tableView.setMaxHeight(300);
        tableView.setMaxWidth(320);

        // TẠO CỘT SỐ LƯỢNG (Quantity Column)
        TableColumn<MenuItem, Integer> quantityColumn = new TableColumn<>("Số lượng");

        // 1. Thiết lập giá trị ban đầu cho cột: Lấy số lượng từ Map tạm
        quantityColumn.setCellValueFactory(cellData -> {
            int itemId = cellData.getValue().getMenuItemId();
            Integer initialQuantity = tempSelectedQuantities.getOrDefault(itemId, 0);
            return new SimpleIntegerProperty(initialQuantity).asObject();
        });

        // 2. Cho phép chỉnh sửa bằng TextField và chỉ chấp nhận số nguyên
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // 3. Cập nhật Map khi người dùng hoàn thành chỉnh sửa
        quantityColumn.setOnEditCommit(e -> {
            MenuItem item = e.getRowValue();
            Integer newQuantity = e.getNewValue();

            if (newQuantity != null && newQuantity > 0) {
                tempSelectedQuantities.put(item.getMenuItemId(), newQuantity);
            } else {
                // Nếu nhập 0 hoặc giá trị không hợp lệ, xóa khỏi danh sách chọn (hoặc đặt về 0)
                tempSelectedQuantities.put(item.getMenuItemId(), 0);
                // Có thể cần refresh lại bảng để hiển thị giá trị 0 nếu người dùng nhập số âm
                tableView.refresh();
            }
        });
        quantityColumn.setPrefWidth(60);

        // THÊM CỘT SỐ LƯỢNG VÀO BẢNG (thay vì cột checkbox)

        // Cột tên món
        TableColumn<MenuItem, String> nameColumn = new TableColumn<>("Tên món");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));
        nameColumn.setPrefWidth(200);

        // Cột số lượng
        TableColumn<MenuItem, Integer> priceColumn = new TableColumn<>("Giá (VNĐ)");
        priceColumn.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getCurrentPrice()).asObject());
        priceColumn.setPrefWidth(80);

        tableView.setEditable(true);
        tableView.getColumns().add(quantityColumn); // Thêm cột mới
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(priceColumn);

        content.getChildren().add(tableView);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Lưu");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16;");

        saveButton.setOnAction(e -> {
            // 1. Kiểm tra điều kiện: Lọc ra các món có số lượng > 0
            List<Map.Entry<Integer, Integer>> selectedEntries = tempSelectedQuantities.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .collect(Collectors.toList());

            if (selectedEntries.isEmpty()) {
                showAlert("Thông báo", "Vui lòng nhập số lượng cho ít nhất một món!");
                return;
            }

            // 2. Chuẩn bị dữ liệu cần thiết
            BookingDetail bookingDetail = bookingDetailService
                    .getBookingDetailNewlestByTableID(selectedTable.getTableID());
            int invoiceId = bookingDetail.getInvoiceID();

            // Tạo Map MenuItemId -> MenuItem từ danh sách gốc để truy cập nhanh chóng
            Map<Integer, MenuItem> menuMap = menuItems.stream()
                    .collect(Collectors.toMap(MenuItem::getMenuItemId, item -> item));

            // 3. Xử lý lưu các món đã chọn (chỉ các mục có Quantity > 0)
            for (Map.Entry<Integer, Integer> entry : selectedEntries) {
                int itemId = entry.getKey();
                int quantity = entry.getValue();
                MenuItem item = menuMap.get(itemId); 

                if (item != null) {
                    int price = item.getCurrentPrice();
                    int total = quantity * price;

                    InvoiceDetail detail = new InvoiceDetail(invoiceId, itemId, quantity, price, total);
                    invoiceDetailService.addInvoiceDetail(detail);
                }
            }

            // 4. Cập nhật tổng hóa đơn
            List<InvoiceDetail> updatedDetails = invoiceDetailService.getInvoiceDetailByInvoiceID(invoiceId);
            int newTotalAmount = updatedDetails.stream()
                    .mapToInt(InvoiceDetail::getLineTotal)
                    .sum();

            Invoice invoiceToUpdate = invoiceService.getInvoiceByInvoiceID(invoiceId);
            invoiceToUpdate.setTotalAmount(newTotalAmount);
            invoiceService.save(invoiceToUpdate);

            dialog.close();
            Platform.runLater(() -> showAlert("Thành công", "Đã thêm món vào hóa đơn!"));
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
            // Không cần làm gì đặc biệt khi đóng
        });

        dialog.showAndWait();
    }

    @FXML
    private void payment() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để thanh toán!");
            return;
        }

        if ("Trống".equals(selectedTable.getStatus())) {
            showAlert("Lỗi", "Bàn này chưa được đặt!");
            return;
        }

        if (listOfInvoiceDetailOfSelectedTable.isEmpty()) {
            showAlert("Lỗi", "Không có món ăn nào để thanh toán!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/payment_dialog.fxml"));
            Parent root = loader.load();

            PaymentDialogController dialogController = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Thanh toán - Bàn " + selectedTable.getTableName());
            dialog.setDialogPane((DialogPane) root);

            dialogController.setTable(selectedTable);
            dialog.showAndWait();

            tableService.changeStatusTable(selectedTable);

            updateTableInfo();
            refreshTableGrid();

                // List<InvoiceDetail> done =
                // FXCollections.observableArrayList(listOfInvoiceDetailOfSelectedTable);
                // int priceTotal = done.stream().mapToInt(InvoiceDetail::getLineTotal).sum();

            

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
}
