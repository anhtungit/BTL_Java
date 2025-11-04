package org.openjfx.Controllers;

import org.openjfx.App;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.openjfx.entity.Account;
import org.openjfx.entity.Employee;
import org.openjfx.service.AccountService;
import org.openjfx.service.EmployeeService;
import org.openjfx.service.impl.AccountServiceImpl;
import org.openjfx.service.impl.EmployeeServiceImpl;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private javafx.scene.control.Button loginButton;

    AccountService accountService = new AccountServiceImpl();
    EmployeeService employeeService = new EmployeeServiceImpl();

    @FXML
    private void initialize() {
        // Pressing Enter in usernameField moves focus to passwordField
        if (usernameField != null) {
            usernameField.setOnAction(e -> {
                if (passwordField != null) passwordField.requestFocus();
            });
        }

        // Pressing Enter in passwordField triggers login
        if (passwordField != null) {
            passwordField.setOnAction(e -> onLogin());
        }
    }

    @FXML
    private void onLogin() {
//       String username = usernameField.getText();
//       String password = passwordField.getText();
         String username = "manager";
         String password = "hashed_password_456";
        Account account = accountService.getAccountByUserName(username);

        if (account != null && account.getPassword().equals(password)) {
            Employee employee = employeeService.getEmployeeByAccountID(account.getAccountId());
            App.setEmployeeLogin(employee);
            try {
                App.setRoot("primary");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi hệ thống");
                alert.setHeaderText(null);
                alert.setContentText("Không thể chuyển đến trang chính: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace(); 
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


