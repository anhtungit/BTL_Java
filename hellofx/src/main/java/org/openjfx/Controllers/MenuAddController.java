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

public class MenuAddController {

    @FXML private TextField txtTenMon;
    @FXML private TextField txtGiaTien;
    @FXML private TableView<Ingredient> tableThanhPhan;
    @FXML private TableColumn<Ingredient, String> colTenThanhPhan;
    @FXML private TableColumn<Ingredient, String> colKhoiLuong;
    @FXML private TableColumn<Ingredient, String> colDonVi;

    private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colTenThanhPhan.setCellValueFactory(cell -> cell.getValue().tenThanhPhanProperty());
        colKhoiLuong.setCellValueFactory(cell -> cell.getValue().khoiLuongProperty());
        colDonVi.setCellValueFactory(cell -> cell.getValue().donViProperty());
        tableThanhPhan.setItems(ingredients);

        tableThanhPhan.setEditable(true);

        colTenThanhPhan.setCellFactory(TextFieldTableCell.forTableColumn());
        colKhoiLuong.setCellFactory(TextFieldTableCell.forTableColumn());
        colDonVi.setCellFactory(TextFieldTableCell.forTableColumn());

        colTenThanhPhan.setOnEditCommit(event -> event.getRowValue().setTenThanhPhan(event.getNewValue()));
        colKhoiLuong.setOnEditCommit(event -> event.getRowValue().setKhoiLuong(event.getNewValue()));
        colDonVi.setOnEditCommit(event -> event.getRowValue().setDonVi(event.getNewValue()));

        for(int i=0;i<3;i++) {
            ingredients.add(new Ingredient("", "", ""));
        }
    }

    @FXML
    private void handleThemMon() {
        String ten = txtTenMon.getText().trim();
        String giaText = txtGiaTien.getText().trim();

        if (ten.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên món!");
            return;
        }

        if (giaText.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập giá tiền!");
            return;
        }

        try {
            String cleanGiaText = giaText.replaceAll("[^0-9]", "");
            double gia = Double.parseDouble(cleanGiaText);
            
            if (gia <= 0) {
                showAlert("Lỗi", "Giá tiền phải lớn hơn 0!");
                return;
            }

            MenuStore.addItem(new MenuItem(ten, gia));
            
            txtTenMon.clear();
            txtGiaTien.clear();
            ingredients.clear();
            for(int i=0;i<3;i++) {
                ingredients.add(new Ingredient("", "", ""));
            }
            
            showAlert("Thành công!", "Đã thêm món \"" + ten + "\" vào thực đơn!");
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá tiền không hợp lệ! Vui lòng chỉ nhập số.");
        }
    }

    @FXML
    private void handleHuy() {
        txtTenMon.clear();
        txtGiaTien.clear();
        ingredients.clear();
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
