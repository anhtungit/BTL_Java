package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SalesController implements Initializable {
    
    @FXML private GridPane tableGrid;
    @FXML private Button btnViewTable;
    @FXML private Button btnTransferTable;
    @FXML private Button btnSplitTable;
    @FXML private Button btnCombineTable;
    @FXML private Button btnCancelTable;
    @FXML private Button btnReserveTable;
    @FXML private Button btnSelectMenu;
    @FXML private Button btnPayment;
    @FXML private Button btnPrint;
    @FXML private Label lblSelectedTable;
    @FXML private Label lblTableStatus;
    @FXML private Label lblCustomerName;
    @FXML private VBox reserveDialog;
    @FXML private TextField txtCustomerName;
    
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
        
        // Thiết lập màu sắc dựa trên trạng thái
        updateTableButtonStyle(button, table);
        
        // Xử lý sự kiện click
        button.setOnAction(e -> selectTable(table));
        
        return button;
    }

    private void updateTableButtonStyle(Button button, Table table) {
        String style = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; ";
        
        if (table.isEmpty()) {
            style += "-fx-background-color: #e0e0e0; -fx-border-color: #9e9e9e;";
        } else if (table.isOccupied()) {
            style += "-fx-background-color: #ffcdd2; -fx-border-color: #f44336;";
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
            case "occupied": return "Có khách";
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
        
        String info = "Bàn: " + selectedTable.getTableNumber() + "\n" +
                     "Trạng thái: " + getStatusText(selectedTable.getStatus()) + "\n" +
                     "Khách hàng: " + (selectedTable.getCustomerName().isEmpty() ? "Chưa có" : selectedTable.getCustomerName()) + "\n" +
                     "Sức chứa: " + selectedTable.getCapacity() + " người";
        
        showAlert("Thông tin bàn", info);
    }

    @FXML
    private void transferTable() {
        if (selectedTable == null || !selectedTable.isOccupied()) {
            showAlert("Lỗi", "Vui lòng chọn bàn có khách để chuyển!");
            return;
        }
        
        // Hiển thị dialog chọn bàn đích
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
        
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(availableTables.get(0), availableTables);
        dialog.setTitle("Chuyển bàn");
        dialog.setHeaderText("Chọn bàn đích:");
        dialog.setContentText("Bàn nguồn: " + selectedTable.getTableNumber());
        
        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            tableStore.transferTable(selectedTable.getTableNumber(), result.get());
            selectedTable = tableStore.getTable(result.get());
            updateTableInfo();
            refreshTableGrid();
            showAlert("Thành công", "Đã chuyển bàn thành công!");
        }
    }

    @FXML
    private void splitTable() {
        if (selectedTable == null || !selectedTable.isOccupied()) {
            showAlert("Lỗi", "Vui lòng chọn bàn có khách để tách!");
            return;
        }
        
        // Hiển thị dialog chọn bàn để tách
        List<Integer> availableTables = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Table table = tableStore.getTable(i);
            if (table != null && table.isEmpty() && i != selectedTable.getTableNumber()) {
                availableTables.add(i);
            }
        }
        
        if (availableTables.isEmpty()) {
            showAlert("Lỗi", "Không có bàn trống để tách!");
            return;
        }
        
        // Tạo dialog tùy chỉnh để chọn nhiều bàn
        Dialog<List<Integer>> dialog = new Dialog<>();
        dialog.setTitle("Tách bàn");
        dialog.setHeaderText("Chọn các bàn để tách:");
        
        // Tạo checkbox cho mỗi bàn trống
        VBox vbox = new VBox();
        List<CheckBox> checkBoxes = new ArrayList<>();
        
        for (Integer tableNum : availableTables) {
            CheckBox checkBox = new CheckBox("Bàn " + String.format("%02d", tableNum));
            checkBoxes.add(checkBox);
            vbox.getChildren().add(checkBox);
        }
        
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<List<Integer>> result = dialog.showAndWait();
        if (result.isPresent()) {
            List<Integer> selectedTables = new ArrayList<>();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selectedTables.add(availableTables.get(i));
                }
            }
            
            if (!selectedTables.isEmpty()) {
                tableStore.splitTable(selectedTable.getTableNumber(), selectedTables);
                refreshTableGrid();
                showAlert("Thành công", "Đã tách bàn thành công!");
            }
        }
    }

    @FXML
    private void combineTable() {
        showAlert("Thông báo", "Chức năng gộp bàn đang được phát triển!");
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
        
        reserveDialog.setVisible(true);
    }

    @FXML
    private void confirmReserve() {
        String customerName = txtCustomerName.getText().trim();
        if (customerName.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên khách hàng!");
            return;
        }
        
        tableStore.reserveTable(selectedTable.getTableNumber(), customerName);
        updateTableInfo();
        refreshTableGrid();
        reserveDialog.setVisible(false);
        txtCustomerName.clear();
        showAlert("Thành công", "Đã đặt bàn thành công!");
    }

    @FXML
    private void cancelReserve() {
        reserveDialog.setVisible(false);
        txtCustomerName.clear();
    }

    @FXML
    private void selectMenu() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để chọn thực đơn!");
            return;
        }
        
        showAlert("Thông báo", "Chức năng chọn thực đơn đang được phát triển!");
    }

    @FXML
    private void payment() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để thanh toán!");
            return;
        }
        
        Order order = tableStore.getOrderByTable(selectedTable.getTableNumber());
        if (order == null) {
            showAlert("Lỗi", "Bàn này chưa có đơn hàng!");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thanh toán");
        alert.setHeaderText("Thanh toán cho bàn " + selectedTable.getTableNumber());
        alert.setContentText("Tổng tiền: " + String.format("%.0f VND", order.getTotalAmount()));
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tableStore.payOrder(order);
            updateTableInfo();
            refreshTableGrid();
            showAlert("Thành công", "Thanh toán thành công!");
        }
    }

    @FXML
    private void print() {
        if (selectedTable == null) {
            showAlert("Lỗi", "Vui lòng chọn bàn để in!");
            return;
        }
        
        showAlert("Thông báo", "Chức năng in ấn đang được phát triển!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
