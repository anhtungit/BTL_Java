package org.openjfx.Controllers;

import java.io.IOException;

import org.openjfx.App;
import org.openjfx.Models.Employee;
import org.openjfx.Stores.EmployeeStore;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    

    @FXML
    private void noop() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Tính năng sẽ được bổ sung sau.");
        alert.showAndWait();
    }

    @FXML
    private void showQuanLyBanHang() throws IOException {
        Node salesView = FXMLLoader.load(getClass().getResource("/org/openjfx/sales-management.fxml"));
        contentPane.getChildren().setAll(salesView);
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

    @FXML private StackPane contentPane;
    @FXML private javafx.scene.layout.BorderPane employeeContent; // in employee_root.fxml

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

        // default content is home greeting
        try {
            showTrangChu();
        } catch (IOException ignored) {}
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

    @FXML
    private void showTrangCaNhan() throws IOException {
        Node profileView = FXMLLoader.load(getClass().getResource("/org/openjfx/profile.fxml"));
        contentPane.getChildren().setAll(profileView);
    }

    @FXML
    private void showTrangChu() throws IOException {
        Node homeView = FXMLLoader.load(getClass().getResource("/org/openjfx/home.fxml"));
        contentPane.getChildren().setAll(homeView);
    }

    @FXML
    private void showQuanLyKhoHang() throws IOException {
        Node inventoryView = FXMLLoader.load(getClass().getResource("/org/openjfx/inventory-view.fxml"));
        contentPane.getChildren().setAll(inventoryView);
    }

    // EMPLOYEE MODULE HOST
    @FXML
    private void empShowList() throws IOException {
        loadEmployeeModule("employee_list.fxml");
    }

    @FXML
    private void empShowAdd() throws IOException {
        loadEmployeeModule("employee_add.fxml");
    }

    @FXML
    private void empShowEdit() throws IOException {
        loadEmployeeModule("employee_edit.fxml");
    }

    @FXML
    private void empShowDelete() throws IOException {
        // Load the employee module root and set the center to the delete view (binds delete table)
        loadEmployeeModule("employee_delete.fxml");
    }

    @FXML
    private void empShowSearch() throws IOException {
        loadEmployeeModule("employee_search.fxml");
    }

    private void loadEmployeeModule(String centerFxml) throws IOException {
        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/org/openjfx/employee_root.fxml"));
        rootLoader.setController(this);
        Node root = rootLoader.load();

        FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/org/openjfx/" + centerFxml));
        centerLoader.setController(this);
        Node center = centerLoader.load();

        if (employeeContent != null) {
            employeeContent.setCenter(center);
        }
        contentPane.getChildren().setAll(root);
        // populate tables if needed
        if ("employee_list.fxml".equals(centerFxml)) bindEmployeeTable();
        if ("employee_delete.fxml".equals(centerFxml)) bindDeleteTable();
        if ("employee_search.fxml".equals(centerFxml)) bindSearchTable(EmployeeStore.getEmployees());
    }

    // TABLE BINDINGS
    @FXML private javafx.scene.control.TableView<Employee> employeeTable;
    @FXML private javafx.scene.control.TableColumn<Employee, String> colName;
    @FXML private javafx.scene.control.TableColumn<Employee, String> colPosition;
    @FXML private javafx.scene.control.TableColumn<Employee, String> colSalary;

    private void bindEmployeeTable() {
        if (employeeTable == null) return;
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPosition()));
        colSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(c.getValue().getSalary())));
        employeeTable.setItems(EmployeeStore.getEmployees());
    }

    private String formatSalary(long vnd) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return nf.format(vnd);
    }

    // DELETE TABLE
    @FXML private javafx.scene.control.TableView<Employee> deleteTable;
    @FXML private javafx.scene.control.TableColumn<Employee, String> delColName;
    @FXML private javafx.scene.control.TableColumn<Employee, String> delColPosition;
    @FXML private javafx.scene.control.TableColumn<Employee, String> delColSalary;

    private void bindDeleteTable() {
        if (deleteTable == null) return;
        delColName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        delColPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPosition()));
        delColSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(c.getValue().getSalary())));
        deleteTable.setItems(EmployeeStore.getEmployees());

        // Only trigger confirm when clicking a real row (not empty space)
        deleteTable.setRowFactory(tv -> {
            final javafx.scene.control.TableRow<Employee> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty()) {
                    Employee selected = row.getItem();
                    javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Bạn có muốn xóa bản ghi này hay không ?");
                    java.util.Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                        EmployeeStore.getEmployees().remove(selected);
                    }
                }
            });
            return row;
        });
    }

    @FXML
    private void empDeleteConfirm() throws IOException {
        Employee selected = deleteTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EmployeeStore.getEmployees().remove(selected);
        }
        empShowDelete();
    }

    // SEARCH TABLE
    @FXML private javafx.scene.control.TextField searchField;
    @FXML private javafx.scene.control.TableView<Employee> searchTable;
    @FXML private javafx.scene.control.TableColumn<Employee, String> sColName;
    @FXML private javafx.scene.control.TableColumn<Employee, String> sColPosition;
    @FXML private javafx.scene.control.TableColumn<Employee, String> sColSalary;

    private void bindSearchTable(javafx.collections.ObservableList<Employee> data) {
        if (searchTable == null) return;
        sColName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        sColPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPosition()));
        sColSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(c.getValue().getSalary())));
        searchTable.setItems(data);
    }

    @FXML
    private void empSearchSubmit() throws IOException {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        javafx.collections.ObservableList<Employee> filtered = EmployeeStore.getEmployees().filtered(e -> e.getFullName().toLowerCase().contains(query));
        bindSearchTable(filtered);
    }

    // ADD / EDIT actions (minimal demo behavior)
    @FXML private javafx.scene.control.TextField addFullName, addAddress, addPosition, addSalary, addPhone, addUsername;
    @FXML private javafx.scene.control.PasswordField addPassword;
    @FXML private javafx.scene.control.TextField editFullName, editAddress, editPosition, editSalary, editPhone, editUsername;
    @FXML private javafx.scene.control.PasswordField editPassword;

    @FXML
    private void empAddSubmit() throws IOException {
        String name = text(addFullName);
        String position = text(addPosition);
        long salary = parseLong(text(addSalary));
        String phone = text(addPhone);
        String username = text(addUsername);
        String password = addPassword == null ? "" : addPassword.getText();
        if (!name.isEmpty() && !position.isEmpty() && salary > 0) {
            EmployeeStore.getEmployees().add(new Employee(name, position, salary, phone, username, password));
            empShowList();
        }
    }

    @FXML
    private void empEditSubmit() throws IOException {
        // For brevity, edit updates first selected in list view
        if (employeeTable != null) {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setFullName(text(editFullName));
                selected.setPosition(text(editPosition));
                selected.setSalary(parseLong(text(editSalary)));
                selected.setPhone(text(editPhone));
                selected.setUsername(text(editUsername));
                if (editPassword != null && !editPassword.getText().isEmpty()) {
                    selected.setPassword(editPassword.getText());
                }
            }
        }
        empShowList();
    }

    @FXML
    private void empBackToList() throws IOException {
        empShowList();
    }

    private String text(javafx.scene.control.TextField tf) { return tf == null ? "" : tf.getText(); }
    private long parseLong(String s) {
        try { return Long.parseLong(s.replace(".", "").replace(",", "").trim()); } catch (Exception e) { return 0; }
    }

    @FXML
    private void showQuanLyThucDon() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/org/openjfx/menu_management.fxml"));
        contentPane.getChildren().setAll(view);
    }

}
