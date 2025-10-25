package org.openjfx.Controllers;

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

    @Override
    public void initialize(URL location, java.util.ResourceBundle resources) {
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
        replaceLabelWithTextField(lblPosition, store.getPosition());
        replaceLabelWithTextField(lblSalary, store.getSalary());
        replaceLabelWithPasswordField(lblPassword);
    }

    private void replaceLabelWithTextField(Label label, String defaultValue) {
        TextField textField = new TextField(defaultValue);
        textField.setPrefWidth(200);
        
        if (label.getParent() instanceof javafx.scene.layout.HBox) {
            javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) label.getParent();
            int index = parent.getChildren().indexOf(label);
            parent.getChildren().set(index, textField);
        }
    }

    private void replaceLabelWithPasswordField(Label label) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setText("123456");
        
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
            UserStore store = UserStore.getInstance();
            
            if (lblFullName.getParent() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) lblFullName.getParent();
                TextField tf = (TextField) parent.getChildren().get(parent.getChildren().indexOf(lblFullName));
                store.setFullName(tf.getText());
            }
            
            if (lblAddress.getParent() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) lblAddress.getParent();
                TextField tf = (TextField) parent.getChildren().get(parent.getChildren().indexOf(lblAddress));
                store.setAddress(tf.getText());
            }
            
            if (lblPhone.getParent() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) lblPhone.getParent();
                TextField tf = (TextField) parent.getChildren().get(parent.getChildren().indexOf(lblPhone));
                store.setPhone(tf.getText());
            }
            
            if (lblPosition.getParent() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) lblPosition.getParent();
                TextField tf = (TextField) parent.getChildren().get(parent.getChildren().indexOf(lblPosition));
                store.setPosition(tf.getText());
            }
            
            if (lblSalary.getParent() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) lblSalary.getParent();
                TextField tf = (TextField) parent.getChildren().get(parent.getChildren().indexOf(lblSalary));
                store.setSalary(tf.getText());
            }
            
            if (lblPassword.getParent() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox parent = (javafx.scene.layout.HBox) lblPassword.getParent();
                PasswordField pf = (PasswordField) parent.getChildren().get(parent.getChildren().indexOf(lblPassword));
                if (!pf.getText().isEmpty()) {
                    store.setPassword(pf.getText());
                }
            }
            
            showAlert("Thành công", "Đã lưu thông tin thành công!");
            
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
