package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple hardcoded accounts for demo purposes
        // manager: admin / 123456
        // staff:   user_one / 123456
        String role = null;
        if ("admin".equals(username) && "123456".equals(password)) {
            role = "MANAGER";
        } else if ("user_one".equals(username) && "123456".equals(password)) {
            role = "STAFF";
        }

        if (role != null) {
            try {
                App.setCurrentUser(username, role);
                App.setRoot("primary");
            } catch (Exception ignored) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Đăng nhập thất bại");
            alert.setHeaderText(null);
            alert.setContentText("Sai tên đăng nhập hoặc mật khẩu.");
            alert.showAndWait();
        }
    }
}


