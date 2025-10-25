package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import java.text.NumberFormat;
import java.util.Locale;

public class PaymentDialogController implements Initializable {
    
    @FXML private DialogPane dialogPane;
    @FXML private Label headerLabel;
    @FXML private TableView<OrderItem> orderTable;
    @FXML private TableColumn<OrderItem, String> itemNameColumn;
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML private TableColumn<OrderItem, String> priceColumn;
    @FXML private Label totalLabel;
    @FXML private TextField amountPaidField;
    @FXML private Label changeLabel;
    @FXML private CheckBox resetTableCheckBox;
    
    private Table currentTable;
    private double totalAmount = 0;
    private TableStore tableStore;
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableStore = TableStore.getInstance();
        setupTable();
        
        // Khởi tạo nút trước khi thiết lập listener
        setupDialogButtons();
        setupAmountPaidListener();
        
        resetTableCheckBox.setSelected(true);
        
        // Set default text cho các label
        changeLabel.setText("0 đ");
        totalLabel.setText("0 đ");
    }
    
    private void setupTable() {
        itemNameColumn.setCellValueFactory(data -> data.getValue().itemNameProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(data -> {
            OrderItem item = data.getValue();
            String formattedPrice = currencyFormat.format(item.getQuantity() * item.getPrice());
            return javafx.beans.binding.Bindings.createStringBinding(() -> formattedPrice);
        });
    }
    
    private void setupAmountPaidListener() {
        amountPaidField.textProperty().addListener((obs, oldText, newText) -> {
            updatePaymentControls();
        });
    }

    private void updatePaymentControls() {
        try {
            String inputText = amountPaidField.getText();
            if (inputText == null || inputText.isEmpty()) {
                changeLabel.setText("0 đ");
                setPayButtonEnabled(false);
                return;
            }

            // Chỉ cho phép nhập số
            String cleanText = inputText.replaceAll("[^0-9]", "");
            if (!cleanText.equals(inputText)) {
                amountPaidField.setText(cleanText);
                return;
            }

            long amountPaid = Long.parseLong(cleanText);
            long change = amountPaid - (long)totalAmount;
            
            // Cập nhật label tiền thừa
            changeLabel.setText(currencyFormat.format(Math.max(0, change)) + " đ");
            
            // Enable nút thanh toán nếu số tiền đủ
            setPayButtonEnabled(amountPaid >= totalAmount);
            
        } catch (NumberFormatException e) {
            changeLabel.setText("0 đ");
            setPayButtonEnabled(false);
        }
    }

    private void setPayButtonEnabled(boolean enabled) {
        ButtonType payButtonType = dialogPane.getButtonTypes().stream()
            .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
            .findFirst()
            .orElse(null);
            
        if (payButtonType != null) {
            Button payButton = (Button) dialogPane.lookupButton(payButtonType);
            if (payButton != null) {
                payButton.setDisable(!enabled);
            }
        }
    }
    
    private void setupDialogButtons() {
        ButtonType payButtonType = new ButtonType("Thanh toán", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().setAll(payButtonType, cancelButtonType);
        
        // Style and configure the pay button
        Button payButton = (Button) dialogPane.lookupButton(payButtonType);
        if (payButton != null) {
            payButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            // Initially disabled until valid amount entered
            payButton.setDisable(true);
            
            // Thêm sự kiện click cho nút thanh toán
            payButton.setOnAction(e -> {
                try {
                    // Hiện thông báo thành công
                    showSuccessAlert();
                    
                    // Thêm nút OK để đóng dialog
                    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    if (!dialogPane.getButtonTypes().contains(okButtonType)) {
                        dialogPane.getButtonTypes().add(okButtonType);
                    }
                    
                    // Tự động click nút OK
                    Button okButton = (Button) dialogPane.lookupButton(okButtonType);
                    if (okButton != null) {
                        okButton.fire();
                    }
                } catch (Exception ex) {
                    // Log lỗi nếu có
                    System.err.println("Lỗi khi đóng dialog: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }

        // Style the cancel button
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        if (cancelButton != null) {
            cancelButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        }
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        
        // Tạo nội dung thông báo chi tiết
        StringBuilder message = new StringBuilder();
        message.append("Thanh toán thành công!\n\n");
        message.append(String.format("Tổng tiền: %s đ\n", currencyFormat.format(totalAmount)));
        
        String amountPaid = amountPaidField.getText();
        long paid = Long.parseLong(amountPaid);
        long change = paid - (long)totalAmount;
        
        message.append(String.format("Khách đưa: %s đ\n", currencyFormat.format(paid)));
        message.append(String.format("Tiền thừa: %s đ", currencyFormat.format(change)));
        
        alert.setContentText(message.toString());
        alert.showAndWait();
    }
    
    public void setTable(Table table) {
        this.currentTable = table;
        headerLabel.setText("Thanh toán - Bàn " + table.getTableNumber());
        
        // Load order items
        ObservableList<OrderItem> items = FXCollections.observableArrayList();
        java.util.List<OrderItem> tableItems = tableStore.getItemsForTable(table.getTableNumber());
        if (tableItems != null && !tableItems.isEmpty()) {
            items.addAll(tableItems);
            orderTable.setItems(items);
            
            // Calculate total
            totalAmount = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
            totalLabel.setText(currencyFormat.format(totalAmount) + " đ");
            
            // Show order summary immediately
            showOrderSummary();
        } else {
            // Show message if no items
            showAlert("Thông báo", "Bàn này chưa có món ăn nào!");
            totalAmount = 0;
            totalLabel.setText("0 đ");
        }
        
        // Reset và cập nhật các control
        amountPaidField.clear();
        changeLabel.setText("0 đ");
        setPayButtonEnabled(false);
    }
    
    private void showOrderSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Chi tiết hóa đơn - Bàn ").append(currentTable.getTableNumber()).append("\n\n");
        
        // Add items
        for (OrderItem item : orderTable.getItems()) {
            summary.append(String.format("%-25s", item.getItemName()))
                   .append(String.format("x%-3d", item.getQuantity()))
                   .append(String.format("%,15d VNĐ\n", (long)(item.getQuantity() * item.getPrice())));
        }
        
        // Add total
        summary.append("\n").append("=".repeat(45)).append("\n");
        summary.append(String.format("%-29s%,15d VNĐ", "Tổng cộng:", (long)totalAmount));
        
        showAlert("Chi tiết thanh toán", summary.toString());
    }
    
    public boolean isResetTableSelected() {
        return resetTableCheckBox.isSelected();
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}