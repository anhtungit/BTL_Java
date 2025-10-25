package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

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

    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
        loadUserData();
    }

    private void loadUserData() {
        // Load current user data from App
        String username = App.getCurrentUsername();
        
        // For demo purposes, set some sample data
        // In real application, you would load from database
        lblFullName.setText("Lê Văn Thể");
        lblAddress.setText("12 Ngô Quyền, tp Đà Nẵng");
        lblPhone.setText("0945423984");
        lblUsername.setText(username != null ? username : "vanthe14");
        lblPosition.setText("Nhân viên pha chế");
        lblSalary.setText("5.000.000");
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
        
        // Replace labels with text fields
        replaceLabelWithTextField(lblFullName, "Lê Văn Thể");
        replaceLabelWithTextField(lblAddress, "12 Ngô Quyền, tp Đà Nẵng");
        replaceLabelWithTextField(lblPhone, "0945423984");
        replaceLabelWithTextField(lblPosition, "Nhân viên pha chế");
        replaceLabelWithTextField(lblSalary, "5.000.000");
        replaceLabelWithPasswordField(lblPassword);
    }

    private void replaceLabelWithTextField(Label label, String defaultValue) {
        TextField textField = new TextField(defaultValue);
        textField.setPrefWidth(200);
        
        // Replace the label in its parent container
        if (label.getParent() instanceof javafx.scene.layout.HBox) {
            javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) label.getParent();
            int index = parent.getChildren().indexOf(label);
            parent.getChildren().set(index, textField);
        }
    }

    private void replaceLabelWithPasswordField(Label label) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setText("123456"); // Default password for editing
        
        // Replace the label in its parent container
        if (label.getParent() instanceof javafx.scene.layout.HBox) {
            javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) label.getParent();
            int index = parent.getChildren().indexOf(label);
            parent.getChildren().set(index, passwordField);
        }
    }

    private void saveProfile() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Lưu thông tin");
        alert.setContentText("Bạn có chắc muốn lưu thông tin đã chỉnh sửa?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // In real application, save to database here
            showAlert("Thành công", "Đã lưu thông tin thành công!");
            
            // Reset to view mode
            isEditing = false;
            btnEdit.setText("Chỉnh sửa");
            loadUserData();
        }
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
                // Clear current user session
                App.setCurrentUser(null, null);
                // Navigate back to login
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
