package org.openjfx.Controllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import org.openjfx.App;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.openjfx.entity.Account;
import org.openjfx.entity.Employee;
import org.openjfx.service.AccountService;
import org.openjfx.service.EmployeeService;
import org.openjfx.service.PositionService;
import org.openjfx.service.impl.AccountServiceImpl;
import org.openjfx.service.impl.EmployeeServiceImpl;
import org.openjfx.service.impl.PositionServiceImpl;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    // @FXML
    // private void noop() {
    //     Alert alert = new Alert(Alert.AlertType.INFORMATION);
    //     alert.setTitle("Thông báo");
    //     alert.setHeaderText(null);
    //     alert.setContentText("Tính năng sẽ được bổ sung sau.");
    //     alert.showAndWait();
    // }

    @FXML
    private void showQuanLyBanHang() throws IOException {
        Node salesView = FXMLLoader.load(getClass().getResource("/org/openjfx/sales-management.fxml"));
        contentPane.getChildren().setAll(salesView);
    }

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
    @FXML private javafx.scene.layout.BorderPane employeeContent;

    private EmployeeService employeeService = new EmployeeServiceImpl();
    private PositionService positionService = new PositionServiceImpl();
    private AccountService accountService = new AccountServiceImpl();

    @FXML
    private void initialize() {
        if (App.getEmployeeLogin().getPositionID() != 1) {
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
            App.setEmployeeLogin(null);
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
        if (employeeTable != null && employeeTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một nhân viên để chỉnh sửa");
            alert.showAndWait();
            return;
        }
        loadEmployeeModule("employee_edit.fxml");
    }

    @FXML
    private void empShowDelete() throws IOException {
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
        if ("employee_list.fxml".equals(centerFxml)) bindEmployeeTable();
        if ("employee_delete.fxml".equals(centerFxml)) bindDeleteTable();
        if ("employee_search.fxml".equals(centerFxml)) bindSearchTable(FXCollections.observableArrayList(employeeService.getAllEmployee()));
        if ("employee_add.fxml".equals(centerFxml)) initAddEmployeeForm();
        if ("employee_edit.fxml".equals(centerFxml)) initEditEmployeeForm();
    }

    //Todo: Xong
    private void initAddEmployeeForm() {
        try {
            if (addPosition != null) {
                addPosition.getItems().setAll(positionService.getAllPosition().stream().map(position -> position.getPositionName()).toList());
                if (addPosition.getItems().size() > 0 && addPosition.getValue() == null) {
                    addPosition.setValue(addPosition.getItems().get(0));
                    addSalary.setText(formatSalary(positionService.getPositionByPositionName(addPosition.getValue()).getSalary()));
                }
                addPosition.setOnAction(e -> {
                    String sel = addPosition.getValue();
                    int s = positionService.getPositionByPositionName(addPosition.getValue()).getSalary();
                    if (addSalary != null) addSalary.setText(formatSalary(s));
                });
            }
        } catch (Exception ignored) {}
    }
    //Todo: Xong
    private void initEditEmployeeForm() {
        try {
            // populate position list
            if (editPosition != null) {
                editPosition.getItems().setAll(positionService.getAllPosition().stream().map(p -> p.getPositionName()).toList());
            }

            Employee selected = (employeeTable == null) ? null : employeeTable.getSelectionModel().getSelectedItem();
            editFullName.setText(selected.getFullName());
            editAddress.setText(selected.getAddress());
            editPhone.setText(selected.getPhoneNumber());
            editUsername.setText(accountService.getAccountByAccountID(selected.getAccountID()).getUsername());
            editPosition.setValue(positionService.getPositionByPositionID(selected.getPositionID()).getPositionName());
            editSalary.setText(formatSalary(positionService.getPositionByPositionID(selected.getPositionID()).getSalary()));
            editPassword.setText("");

            if (editPosition != null) {
                editPosition.setOnAction(e -> {
                    String sel = editPosition.getValue();
                    if (editSalary != null) editSalary.setText(formatSalary(positionService.getPositionByPositionName(sel).getSalary()));
                });
            }
        } catch (Exception ignored) {}
    }

    @FXML private javafx.scene.control.TableView<Employee> employeeTable;
    @FXML private javafx.scene.control.TableColumn<Employee, String> colName;
    @FXML private javafx.scene.control.TableColumn<Employee, String> colPosition;
    @FXML private javafx.scene.control.TableColumn<Employee, String> colSalary;

    //Xong
    private void bindEmployeeTable() {
        if (employeeTable == null) return;
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(positionService.getPositionByPositionID(c.getValue().getPositionID()).getPositionName()));
        colSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(positionService.getPositionByPositionID(c.getValue().getPositionID()).getSalary())));
        employeeTable.setItems(FXCollections.observableArrayList(employeeService.getAllEmployee()));
    }

    private String formatSalary(long vnd) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("vi-VN"));
        return nf.format(vnd);
    }

    @FXML private javafx.scene.control.TableView<Employee> deleteTable;
    @FXML private javafx.scene.control.TableColumn<Employee, String> delColName;
    @FXML private javafx.scene.control.TableColumn<Employee, String> delColPosition;
    @FXML private javafx.scene.control.TableColumn<Employee, String> delColSalary;

    //Xong
    private void bindDeleteTable() {
        if (deleteTable == null) return;
        delColName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        delColPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(positionService.getPositionByPositionID(c.getValue().getPositionID()).getPositionName()));
        delColSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(positionService.getPositionByPositionID(c.getValue().getPositionID()).getSalary())));
        deleteTable.setItems(FXCollections.observableArrayList(employeeService.getAllEmployee()));

        deleteTable.setRowFactory(tv -> {
            final javafx.scene.control.TableRow<Employee> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty()) {
                    Employee selected = row.getItem();
                    System.out.println("Employee is selected to delete: " + selected.getEmployeeID());
                    javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Bạn có muốn xóa nhân viên này hay không ?");
                    java.util.Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                        int accID = selected.getAccountID();
                        accountService.delete(accID);
                        employeeService.delete(selected);
                    }
                }
            });
            return row;
        });
    }

    @FXML private javafx.scene.control.TextField searchField;
    @FXML private javafx.scene.control.TableView<Employee> searchTable;
    @FXML private javafx.scene.control.TableColumn<Employee, String> sColName;
    @FXML private javafx.scene.control.TableColumn<Employee, String> sColPosition;
    @FXML private javafx.scene.control.TableColumn<Employee, String> sColSalary;

    private void bindSearchTable(javafx.collections.ObservableList<Employee> data) {
        if (searchTable == null) return;
        sColName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        sColPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(positionService.getPositionByPositionID(c.getValue().getPositionID()).getPositionName()));
        sColSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(positionService.getPositionByPositionID(c.getValue().getPositionID()).getSalary())));
        searchTable.setItems(data);
    }

    @FXML
    private void empSearchSubmit() throws IOException {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        bindSearchTable(FXCollections.observableArrayList(employeeService.getEmployeeByName(query)));
    }

    @FXML private javafx.scene.control.TextField addFullName, addAddress, addSalary, addPhone, addUsername;
    @FXML private javafx.scene.control.ComboBox<String> addPosition;
    @FXML private javafx.scene.control.PasswordField addPassword;
    @FXML private javafx.scene.control.TextField editFullName, editAddress, editSalary, editPhone, editUsername;
    @FXML private javafx.scene.control.ComboBox<String> editPosition;
    @FXML private javafx.scene.control.PasswordField editPassword;

    //Todo: Xong
    @FXML
    private void empAddSubmit() throws IOException {
        String name = text(addFullName);
        String position = addPosition == null ? "" : addPosition.getValue();
        String phone = text(addPhone);
        String username = "";
        if (!accountService.userNameIsPresent(addUsername.getText()))
            username = text(addUsername);
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thêm nhân viên thất bại!");
            alert.setHeaderText(null);
            alert.setContentText("Tên đăng nhập đã có người sử dụng. Nhập tên đăng nhập khác");
            alert.showAndWait();
            return;
        }
        String password = addPassword == null ? "" : addPassword.getText();
        String address = text(addAddress);
        if (!name.isEmpty() && !position.isEmpty()) {
            int accountID = accountService.create(new Account(username, password));
            int positionID = positionService.getPositionByPositionName(position).getPositionId();
            employeeService.create(new Employee(positionID, accountID, name, phone, address));
            empShowList();
        }
    }

    @FXML
    private void empEditSubmit() throws IOException {
        if (employeeTable != null) {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            Account accSeleted = accountService.getAccountByAccountID(selected.getAccountID());
            if (selected != null) {
                selected.setFullName(text(editFullName));
                selected.setAddress(text(editAddress));
                if (editPosition != null)
                    selected.setPositionID(positionService.getPositionByPositionName(editPosition.getValue()).getPositionId());
                selected.setPhoneNumber(text(editPhone));
                selected.setFullName(text(editUsername));
                if (editPassword != null && !editPassword.getText().isEmpty()) {
                    accSeleted.setPassword(editPassword.getText());
                }
            }
            accountService.save(accSeleted);
            employeeService.save(selected);
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
        Parent view = FXMLLoader.load(getClass().getResource("/org/openjfx/menuitem_management.fxml"));
        contentPane.getChildren().setAll(view);
    }

    @FXML
    private void showQuanLyNganSach() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/org/openjfx/budget_management.fxml"));
        contentPane.getChildren().setAll(view);
    }

    @FXML
    private void handleReportModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/report.fxml"));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
