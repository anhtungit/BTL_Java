package org.openjfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MenuEditController {

    @FXML private TableView<MenuItem> tableMonAn;
    @FXML private TableColumn<MenuItem, String> colTenMon;
    @FXML private TableColumn<MenuItem, String> colGiaTien;

    @FXML private TextField txtTenMon;
    @FXML private TextField txtGiaTien;

    @FXML private Button btnLuu;
    @FXML private Button btnHuy;

    private ObservableList<MenuItem> menuItems;

    @FXML
    public void initialize() {
        // Gán dữ liệu cho các cột
        colTenMon.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colGiaTien.setCellValueFactory(cell ->
            new SimpleStringProperty(String.format("%.0f", cell.getValue().getPrice()))
        );

        // Lấy dữ liệu từ MenuStore
        menuItems = MenuStore.getItems();
        tableMonAn.setItems(menuItems);

        // Khi chọn 1 món → hiển thị vào TextField
        tableMonAn.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtTenMon.setText(newSel.getName());
                txtGiaTien.setText(String.valueOf(newSel.getPrice()));
            }
        });
    }

    @FXML
    private void handleLuu() {
        MenuItem selected = tableMonAn.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Thông báo", "Vui lòng chọn món cần chỉnh sửa!");
            return;
        }

        String tenMoi = txtTenMon.getText().trim();
        String giaMoiStr = txtGiaTien.getText().trim();

        if (tenMoi.isEmpty() || giaMoiStr.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ tên món và giá tiền!");
            return;
        }

        try {
            double giaMoi = Double.parseDouble(giaMoiStr);
            if (giaMoi < 0) {
                showAlert("Lỗi", "Giá tiền phải lớn hơn 0!");
                return;
            }

            selected.setName(tenMoi);
            selected.setPrice(giaMoi);
            tableMonAn.refresh();

            showAlert("Thành công", "Đã cập nhật món \"" + tenMoi + "\"!");
            txtTenMon.clear();
            txtGiaTien.clear();
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá tiền phải là số hợp lệ!");
        }
    }

    @FXML
    private void handleHuy() {
        txtTenMon.clear();
        txtGiaTien.clear();
        tableMonAn.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
