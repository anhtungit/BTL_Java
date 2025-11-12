package org.openjfx.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.openjfx.entity.BookingDetail;
import org.openjfx.entity.Invoice;
import org.openjfx.entity.InvoiceDetail;
import org.openjfx.entity.Table;
import org.openjfx.service.*;
import org.openjfx.service.impl.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentDialogController implements Initializable {
    
    @FXML private DialogPane dialogPane;
    @FXML private Label headerLabel;
    @FXML private TableView<InvoiceDetail> orderTable;
    @FXML private TableColumn<InvoiceDetail, String> itemNameColumn;
    @FXML private TableColumn<InvoiceDetail, Integer> quantityColumn;
    @FXML private TableColumn<InvoiceDetail, String> priceColumn;
    @FXML private Label totalLabel;
    @FXML private TextField amountPaidField;
    @FXML private Label changeLabel;
    
    
    private Table currentTable;
    BookingDetail bookingDetail;
    Invoice invoice;
    List<InvoiceDetail> tableItems;

    private int totalAmount = 0;
    private List<Table> tableList;
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));

    TableService tableService = new TableServiceImpl();
    BookingDetailService bookingDetailService = new BookingDetailServiceImpl();
    InvoiceService invoiceService = new InvoiceServiceImpl();
    InvoiceDetailService invoiceDetailService = new InvoiceDetailServiceImpl();
    MenuItemService menuItemService = new MenuItemServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableList = tableService.getAllTables();
        setupTable();
        
        setupDialogButtons();
        setupAmountPaidListener();
        
        changeLabel.setText("0 đ");
        totalLabel.setText("0 đ");
    }
    
    private void setupTable() {
        itemNameColumn.setCellValueFactory(data -> new SimpleStringProperty(menuItemService.getMenuItemByMenuItemID(data.getValue().getMenuItemID()).getItemName()));
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(data -> {
            InvoiceDetail item = data.getValue();
            String formattedPrice = currencyFormat.format(item.getQuantity() * item.getPriceAtSale());
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

            String cleanText = inputText.replaceAll("[^0-9]", "");
            if (!cleanText.equals(inputText)) {
                amountPaidField.setText(cleanText);
                return;
            }

            long amountPaid = Long.parseLong(cleanText);
            long change = amountPaid - (long)totalAmount;
            
            changeLabel.setText(currencyFormat.format(Math.max(0, change)) + " đ");
            
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
        
        Button payButton = (Button) dialogPane.lookupButton(payButtonType);
        if (payButton != null) {
            payButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            payButton.setDisable(true);
            
            payButton.setOnAction(e -> {
                try {
                    invoice.setStatus(0);
                    invoice.setTotalAmount(totalAmount);
                    invoiceService.save(invoice);

                    showSuccessAlert();
                    
                    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    if (!dialogPane.getButtonTypes().contains(okButtonType)) {
                        dialogPane.getButtonTypes().add(okButtonType);
                    }
                    
                    Button okButton = (Button) dialogPane.lookupButton(okButtonType);
                    if (okButton != null) {
                        okButton.fire();
                    }
                } catch (Exception ex) {
                    System.err.println("Lỗi khi đóng dialog: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }

        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        if (cancelButton != null) {
            cancelButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        }
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        
        try {
            tableService.changeStatusTable(currentTable);
            StringBuilder message = new StringBuilder();
            message.append("Thanh toán thành công!\n\n");
            message.append(String.format("Tổng tiền: %s đ\n", currencyFormat.format(totalAmount)));
            
            String amountPaid = amountPaidField.getText().replaceAll("[^0-9]", "");
            long paid = Long.parseLong(amountPaid);
            long change = paid - (long)totalAmount;
            
            message.append(String.format("Khách đưa: %s đ\n", currencyFormat.format(paid)));
            message.append(String.format("Tiền thừa: %s đ", currencyFormat.format(change)));
            
            alert.setContentText(message.toString());
            alert.showAndWait();
        } catch (Exception ex) {
            alert.setContentText("Thanh toán thành công!");
            alert.showAndWait();
        }
    }
    
    public void setTable(Table table) {
        this.currentTable = table;
        headerLabel.setText("Thanh toán - Bàn " + table.getTableName());

        ObservableList<InvoiceDetail> items = FXCollections.observableArrayList();

        this.bookingDetail = bookingDetailService.getBookingDetailNewlestByTableID(table.getTableID());
        this.invoice = invoiceService.getInvoiceByInvoiceID(bookingDetail.getInvoiceID());
        this.tableItems = invoiceDetailService.getInvoiceDetailByInvoiceID(invoice.getInvoiceID());

        if (tableItems != null && !tableItems.isEmpty()) {
            items.addAll(tableItems);
            orderTable.setItems(items);

            totalAmount = items.stream()
                .mapToInt(item -> item.getQuantity() * item.getPriceAtSale())
                .sum();
            totalLabel.setText(currencyFormat.format(totalAmount) + " đ");
        } else {
            showAlert("Thông báo", "Bàn này chưa có món ăn nào!");
            totalAmount = 0;
            totalLabel.setText("0 đ");
        }

        amountPaidField.clear();
        changeLabel.setText("0 đ");
        setPayButtonEnabled(false);
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