package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.openjfx.App;
import org.openjfx.entity.Account;
import org.openjfx.entity.Employee;
import org.openjfx.entity.Position;
import org.openjfx.service.AccountService;
import org.openjfx.service.EmployeeService;
import org.openjfx.service.PositionService;
import org.openjfx.service.impl.AccountServiceImpl;
import org.openjfx.service.impl.EmployeeServiceImpl;
import org.openjfx.service.impl.PositionServiceImpl;

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
    private final Map<Label, Control> editingControls = new HashMap<>();

    private EmployeeService employeeService = new EmployeeServiceImpl();
    private PositionService positionService = new PositionServiceImpl();
    private AccountService accountService = new AccountServiceImpl();

    private Account accountLogin;
    private Position positionLogin;
    private Employee employeeLogin = App.getEmployeeLogin();

    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        accountLogin = accountService.getAccountByAccountID(employeeLogin.getAccountID());
        positionLogin = positionService.getPositionByPositionID(employeeLogin.getPositionID());
        loadUserData();
    }

    private void loadUserData() {
        lblFullName.setText(employeeLogin.getFullName());
        lblPosition.setText(positionLogin.getPositionName());
        lblAddress.setText(employeeLogin.getAddress());
        lblPhone.setText(employeeLogin.getPhoneNumber());
        lblSalary.setText(String.valueOf(positionLogin.getSalary()));
        lblUsername.setText(accountLogin.getUsername());
        lblPassword.setText("********");
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
        
        replaceLabelWithTextField(lblFullName, employeeLogin.getFullName());
        replaceLabelWithTextField(lblAddress, employeeLogin.getAddress());
        replaceLabelWithTextField(lblPhone, employeeLogin.getPhoneNumber());
        replaceLabelWithTextField(lblPassword, accountLogin.getPassword());
        replacePositionWithComboBox(lblPosition, positionLogin.getPositionName());
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
        List<Position> positionList = positionService.getAllPosition();
        ObservableList<String> positions = FXCollections.observableArrayList(positionList.stream().map(position -> position.getPositionName()).toList());
        ComboBox<String> combo = new ComboBox<>(positions);
        combo.setPrefWidth(200);
        combo.setValue(currentPosition);

        // update salary label immediately when selection changes
        combo.setOnAction(e -> {
            String sel = combo.getValue();
            String salary = String.valueOf(positionService.getPositionByPositionName(sel).getSalary());
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
            // Full name
            Control c = editingControls.get(lblFullName);
            if (c instanceof TextField) employeeLogin.setFullName(((TextField) c).getText());

            // Address
            c = editingControls.get(lblAddress);
            if (c instanceof TextField) employeeLogin.setAddress(((TextField) c).getText());

            // Phone
            c = editingControls.get(lblPhone);
            if (c instanceof TextField) employeeLogin.setPhoneNumber(((TextField) c).getText());

            // Password
            c = editingControls.get(lblPassword);
            if (c instanceof TextField) accountLogin.setPassword(((TextField) c).getText());

            // Position & Salary
            c = editingControls.get(lblPosition);
            if (c instanceof ComboBox) {
                @SuppressWarnings("unchecked")
                ComboBox<String> combo = (ComboBox<String>) c;
                String pos = combo.getValue();
                positionLogin = positionService.getPositionByPositionName(pos);
                employeeLogin.setPositionID(positionLogin.getPositionId());
            }

            // cleanup: restore labels in UI
            restoreLabels();

            showAlert("Thành công", "Đã lưu thông tin thành công!");
        }
        employeeService.save(employeeLogin);
        accountService.save(accountLogin);
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
                App.setEmployeeLogin(null);
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
