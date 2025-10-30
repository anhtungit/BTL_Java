package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.openjfx.App;
import org.openjfx.Stores.UserStore;

public class ProfileController implements Initializable {
    
    @FXML private Label lblFullName;
    @FXML private Label lblAddress;
    @FXML private Label lblPhone;
    @FXML private Label lblUsername;
    @FXML private Label lblPosition;
    @FXML private Label lblSalary;
    @FXML private Label lblPassword;
    @FXML private Button btnEdit;
    @FXML private Button btnLogout;
    
    private boolean isEditing = false;
    private final Map<String, String> positionSalaryMap = new HashMap<>();
    private final Map<Label, Control> editingControls = new HashMap<>();

    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        // define positions and their default salaries
        positionSalaryMap.put("Quản lý", "15.000.000");
        positionSalaryMap.put("Nhân viên pha chế", "7.000.000");
        positionSalaryMap.put("Nhân viên phục vụ", "5.000.000");
        positionSalaryMap.put("Nhân viên bảo vệ", "5.000.000");

        loadUserData();
    }

    private void loadUserData() {
        UserStore store = UserStore.getInstance();
        
        lblFullName.setText(store.getFullName());
        lblAddress.setText(store.getAddress());
        lblPhone.setText(store.getPhone());
        lblUsername.setText(store.getUsername());
        lblPosition.setText(store.getPosition());
        lblSalary.setText(store.getSalary());
        lblPassword.setText("*********");
    }

    @FXML
    private void editProfile() {
        if (!isEditing) {
            startEditing();
        } else {
            saveProfile();
        }
    }

    private void startEditing() {
        isEditing = true;
        btnEdit.setText("Lưu");
        
        UserStore store = UserStore.getInstance();
        
        replaceLabelWithTextField(lblFullName, store.getFullName());
        replaceLabelWithTextField(lblAddress, store.getAddress());
        replaceLabelWithTextField(lblPhone, store.getPhone());

        // Position -> ComboBox (drop list) and auto-update salary label
        replacePositionWithComboBox(lblPosition, store.getPosition());

        // Do NOT replace salary or password: salary is auto-updated and password is not editable here
    }

    private void replaceLabelWithTextField(Label label, String defaultValue) {
        if (!(label.getParent() instanceof HBox)) return;

        TextField textField = new TextField(defaultValue);
        textField.setPrefWidth(200);
        
        HBox parent = (HBox) label.getParent();
        int index = parent.getChildren().indexOf(label);
        parent.getChildren().set(index, textField);

        editingControls.put(label, textField);
    }

    private void replacePositionWithComboBox(Label label, String currentPosition) {
        if (!(label.getParent() instanceof HBox)) return;

        ObservableList<String> positions = FXCollections.observableArrayList(positionSalaryMap.keySet());
        ComboBox<String> combo = new ComboBox<>(positions);
        combo.setPrefWidth(200);
        combo.setValue(currentPosition);

        // update salary label immediately when selection changes
        combo.setOnAction(e -> {
            String sel = combo.getValue();
            String salary = positionSalaryMap.getOrDefault(sel, "0");
            lblSalary.setText(salary);
        });

        HBox parent = (HBox) label.getParent();
        int index = parent.getChildren().indexOf(label);
        parent.getChildren().set(index, combo);

        editingControls.put(label, combo);
    }

    private void saveProfile() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Lưu thông tin");
        alert.setContentText("Bạn có chắc muốn lưu thông tin đã chỉnh sửa?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserStore store = UserStore.getInstance();

            // Full name
            Control c = editingControls.get(lblFullName);
            if (c instanceof TextField) store.setFullName(((TextField) c).getText());

            // Address
            c = editingControls.get(lblAddress);
            if (c instanceof TextField) store.setAddress(((TextField) c).getText());

            // Phone
            c = editingControls.get(lblPhone);
            if (c instanceof TextField) store.setPhone(((TextField) c).getText());

            // Position & Salary
            c = editingControls.get(lblPosition);
            if (c instanceof ComboBox) {
                @SuppressWarnings("unchecked")
                ComboBox<String> combo = (ComboBox<String>) c;
                String pos = combo.getValue();
                store.setPosition(pos);
                String salary = positionSalaryMap.getOrDefault(pos, store.getSalary());
                store.setSalary(salary);
            }

            // cleanup: restore labels in UI
            restoreLabels();

            showAlert("Thành công", "Đã lưu thông tin thành công!");
        }
    }

    private void restoreLabels() {
        isEditing = false;
        btnEdit.setText("Chỉnh sửa");

        // replace controls back with labels using stored keys
        for (Map.Entry<Label, Control> entry : editingControls.entrySet()) {
            Label label = entry.getKey();
            Control control = entry.getValue();
            if (control != null && control.getParent() instanceof HBox) {
                HBox parent = (HBox) control.getParent();
                int idx = parent.getChildren().indexOf(control);
                if (idx >= 0) parent.getChildren().set(idx, label);
            }
        }

        editingControls.clear();
        loadUserData();
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Đăng xuất");
        alert.setContentText("Bạn có chắc muốn đăng xuất?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                App.setCurrentUser(null, null);
                App.setRoot("login");
            } catch (IOException e) {
                showAlert("Lỗi", "Không thể đăng xuất!");
            }
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
