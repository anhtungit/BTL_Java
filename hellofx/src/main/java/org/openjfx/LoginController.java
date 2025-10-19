package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLogin() {
        // TODO: Replace with real authentication
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            try {
                App.setRoot("primary");
            } catch (Exception ignored) {
            }
        }
    }
}


