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

        // Kiểm tra kết nối database trước
        if (!DatabaseConfig.testConnection()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi kết nối");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Không thể kết nối đến database. Vui lòng kiểm tra:\n1. MySQL server đang chạy\n2. Database 'coffee_shop_db' đã được tạo\n3. Thông tin kết nối trong DatabaseConfig.java");
            alert.showAndWait();
            return;
        }

        // Xác thực từ database
        Employee employee = EmployeeStore.authenticateUser(username, password);

        if (employee != null) {
            try {
                App.setCurrentUser(username, employee.getRole());
                App.setRoot("primary");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi hệ thống");
                alert.setHeaderText(null);
                alert.setContentText("Có lỗi xảy ra khi đăng nhập: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Đăng nhập thất bại");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Sai tên đăng nhập hoặc mật khẩu.\n\nTài khoản mặc định:\n- admin1 / 123456 (MANAGER)\n- vanthe14 / 123456 (STAFF)\n- giangtt / 123456 (MANAGER)\n- yenvb / 123456 (STAFF)");
            alert.showAndWait();
        }
    }
}
