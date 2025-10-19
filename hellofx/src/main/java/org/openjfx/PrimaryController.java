package org.openjfx;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void showTrangChu() throws IOException {
        // already on primary view; this hook is for future navigation
    }

    @FXML
    private void noop() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Tính năng sẽ được bổ sung sau.");
        alert.showAndWait();
    }

    // Role-based visibility: Manager sees all; Staff limited
    @FXML private Button btnTrangChu;
    @FXML private Button btnTrangCaNhan;
    @FXML private Button btnQuanLyNhanVien;
    @FXML private Button btnQuanLyBanHang;
    @FXML private Button btnQuanLyTrangThietBi;
    @FXML private Button btnQuanLyKhoHang;
    @FXML private Button btnQuanLyThucDon;
    @FXML private Button btnQuanLyMarketing;
    @FXML private Button btnQuanLyNganSach;
    @FXML private Button btnQuanLyDuLieu;
    @FXML private Button btnThongKeBaoCao;
    @FXML private Button btnGioiThieu;

    @FXML
    private void initialize() {
        String role = App.getCurrentRole();
        if ("STAFF".equals(role)) {
            // Staff can see: Trang chủ, Trang cá nhân, Quản lý bán hàng, Giới thiệu
            setVisible(btnTrangChu, true);
            setVisible(btnTrangCaNhan, true);
            setVisible(btnQuanLyBanHang, true);
            setVisible(btnGioiThieu, true);

            setVisible(btnQuanLyNhanVien, false);
            setVisible(btnQuanLyTrangThietBi, false);
            setVisible(btnQuanLyKhoHang, false);
            setVisible(btnQuanLyThucDon, false);
            setVisible(btnQuanLyMarketing, false);
            setVisible(btnQuanLyNganSach, false);
            setVisible(btnQuanLyDuLieu, false);
            setVisible(btnThongKeBaoCao, false);
        }
        // Managers: no changes; all buttons remain visible
    }

    private void setVisible(Button button, boolean visible) {
        if (button != null) {
            button.setManaged(visible);
            button.setVisible(visible);
        }
    }

    @FXML
    private void logout() {
        try {
            // clear in-memory session
            App.setCurrentUser(null, null);
            App.setRoot("login");
        } catch (IOException ignored) {
        }
    }
}
