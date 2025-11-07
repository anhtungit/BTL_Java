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
        
        // Thêm validation cho từng trường
        if (label == lblPhone) {
            // Chỉ cho phép nhập số và tối đa 10 ký tự cho số điện thoại
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 10) {
                    textField.setText(newValue.substring(0, 10));
                }
            });
        } else if (label == lblFullName) {
            // Không cho phép nhập số trong họ tên
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\p{L}\\s]*")) {
                    textField.setText(newValue.replaceAll("[^\\p{L}\\s]", ""));
                }
            });
        } else if (label == lblPassword) {
            // Giới hạn độ dài mật khẩu
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > 20) {
                    textField.setText(newValue.substring(0, 20));
                }
            });
        }
        
        // Hiển thị viền đỏ nếu trường bị bỏ trống
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                textField.setStyle("-fx-border-color: red;");
            } else {
                textField.setStyle("");
            }
        });
        
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
        // Kiểm tra các trường không được bỏ trống
        
        // Kiểm tra họ tên
        Control fullNameControl = editingControls.get(lblFullName);
        if (fullNameControl instanceof TextField) {
            String fullName = ((TextField) fullNameControl).getText().trim();
            if (fullName.isEmpty()) {
                showAlert("Lỗi", "Họ và tên không được để trống!");
                return;
            }
        }

        // Kiểm tra địa chỉ
        Control addressControl = editingControls.get(lblAddress);
        if (addressControl instanceof TextField) {
            String address = ((TextField) addressControl).getText().trim();
            if (address.isEmpty()) {
                showAlert("Lỗi", "Địa chỉ không được để trống!");
                return;
            }
        }

        // Kiểm tra số điện thoại
        Control phoneControl = editingControls.get(lblPhone);
        if (phoneControl instanceof TextField) {
            String phoneNumber = ((TextField) phoneControl).getText().trim();
            if (phoneNumber.isEmpty()) {
                showAlert("Lỗi", "Số điện thoại không được để trống!");
                return;
            }
            if (phoneNumber.length() < 10) {
                showAlert("Lỗi", "Số điện thoại phải có 10 số!");
                return;
            }
        }

        // Kiểm tra mật khẩu
        Control passwordControl = editingControls.get(lblPassword);
        if (passwordControl instanceof TextField) {
            String password = ((TextField) passwordControl).getText().trim();
            if (password.isEmpty()) {
                showAlert("Lỗi", "Mật khẩu không được để trống!");
                return;
            }
            if (password.length() < 6) {
                showAlert("Lỗi", "Mật khẩu phải có ít nhất 6 ký tự!");
                return;
            }
        }

        // Kiểm tra chức vụ
        Control positionControl = editingControls.get(lblPosition);
        if (positionControl instanceof ComboBox) {
            @SuppressWarnings("unchecked")
            ComboBox<String> combo = (ComboBox<String>) positionControl;
            if (combo.getValue() == null || combo.getValue().trim().isEmpty()) {
                showAlert("Lỗi", "Vui lòng chọn chức vụ!");
                return;
            }
        }

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
