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

        //Cho phép chỉnh sửa bảng
        tableThanhPhan.setEditable(true);

        //Cho phép nhập text trực tiếp vào ô
        colTenThanhPhan.setCellFactory(TextFieldTableCell.forTableColumn());
        colKhoiLuong.setCellFactory(TextFieldTableCell.forTableColumn());
        colDonVi.setCellFactory(TextFieldTableCell.forTableColumn());

        //cập nhật ingredient khi sửa nội dung ô
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
        double gia = Double.parseDouble(txtGiaTien.getText().trim());

        if (ten.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên món!");
            return;
        }

        MenuStore.addItem(new MenuItem(ten, gia));
        showAlert("Thành công!", "Đã thêm món \"" + ten + "\" vào thực đơn!");
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
