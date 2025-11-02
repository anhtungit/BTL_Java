package org.openjfx.Controllers;

import org.openjfx.Stores.MenuStore;
import javafx.scene.control.Alert;

/**
 * Base class chứa các phương thức dùng chung cho MenuItem controllers
 */
public class MenuItemControllerBase {

    protected void reloadData() {
        MenuStore.loadFromDatabase();
    }

    protected void showAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    protected void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void handleDatabaseError(Exception e) {
        String errorMsg = e.getMessage();
        if (errorMsg != null && errorMsg.contains("kết nối")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi kết nối",
                    "Không thể kết nối với database!\n\n" +
                            "Vui lòng kiểm tra:\n" +
                            "1. MySQL đã được khởi động chưa?\n" +
                            "2. Database 'coffee_shop' đã tồn tại chưa?\n" +
                            "3. Username/Password có đúng không?");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi",
                    errorMsg != null ? errorMsg : "Đã xảy ra lỗi!");
        }
        e.printStackTrace();
    }
}
