package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

public class EmployeeManagementController implements Initializable {
    
    @FXML private Button btnViewEmployees;
    @FXML private Button btnAddEmployee;
    @FXML private Button btnEditEmployee;
    @FXML private Button btnDeleteEmployee;
    @FXML private Button btnSearch;
    @FXML private TextField searchField;
    @FXML private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hiển thị danh sách nhân viên khi mở màn hình
        showEmployeeList();
    }

    @FXML
    private void showEmployeeList() {
        try {
            Node view = FXMLLoader.load(getClass().getResource("employee_list.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể tải danh sách nhân viên");
        }
    }

    @FXML
    private void showAddEmployee() {
        try {
            Node view = FXMLLoader.load(getClass().getResource("employee_add.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể tải form thêm nhân viên");
        }
    }

    @FXML
    private void showEditEmployee() {
        try {
            Node view = FXMLLoader.load(getClass().getResource("employee_edit.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể tải form chỉnh sửa nhân viên");
        }
    }

    @FXML
    private void showDeleteEmployee() {
        try {
            Node view = FXMLLoader.load(getClass().getResource("employee_delete.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể tải form xóa nhân viên");
        }
    }

    @FXML
    private void searchEmployees() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            // Implement search functionality here
            System.out.println("Searching for: " + searchTerm);
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}