package org.openjfx.Controllers;

import org.openjfx.entity.MenuItem;
import org.openjfx.service.MenuItemService;
import org.openjfx.service.impl.MenuItemServiceImpl;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MenuItemEditController extends MenuItemControllerBase {

    @FXML
    private TableView<MenuItem> tableMenuItem;
    @FXML
    private TableColumn<MenuItem, String> colItemName;
    @FXML
    private TableColumn<MenuItem, String> colCurrentPrice;

    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtCurrentPrice;

    private ObservableList<MenuItem> menuItems;

    MenuItemService menuItemService = new MenuItemServiceImpl();

    @FXML
    public void initialize() {
        // reloadData();

        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCurrentPrice.setCellValueFactory(
                cell -> new SimpleStringProperty(String.format("%.0f", cell.getValue().getCurrentPrice())));

        menuItems = FXCollections.observableArrayList(menuItemService.getAllMenuItem());
        tableMenuItem.setItems(menuItems);

        tableMenuItem.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtItemName.setText(newSel.getItemName());
                txtCurrentPrice.setText(String.valueOf(newSel.getCurrentPrice()));
            }
        });
    }

    @FXML
    private void handleSave() {
        MenuItem selected = tableMenuItem.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Thông báo", "Vui lòng chọn món cần chỉnh sửa!");
            return;
        }

        String tenMoi = txtItemName.getText().trim();
        String giaMoiStr = txtCurrentPrice.getText().trim();

        if (tenMoi.isEmpty() || giaMoiStr.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ tên món và giá tiền!");
            return;
        }

        try {
            int giaMoi = Integer.parseInt(giaMoiStr);
            if (giaMoi < 0) {
                showAlert("Lỗi", "Giá tiền phải lớn hơn 0!");
                return;
            }

            selected.setItemName(tenMoi);
            selected.setCurrentPrice(giaMoi);
            menuItemService.updateMenuItem(selected);
            // reloadData();
            tableMenuItem.refresh();

            showAlert("Thành công", "Đã cập nhật món \"" + tenMoi + "\"!");
            txtItemName.clear();
            txtCurrentPrice.clear();
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá tiền phải là số hợp lệ!");
        } catch (RuntimeException e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void handleCancel() {
        txtItemName.clear();
        txtCurrentPrice.clear();
        tableMenuItem.getSelectionModel().clearSelection();
    }
}
