package org.openjfx.Controllers;

import java.io.IOException;

import org.openjfx.App;
import org.openjfx.Models.EmployeeForm;
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
        if ("employee_search.fxml".equals(centerFxml)) bindSearchTable(EmployeeStore.getEmployees());
        // Initialize employee form controls when showing add/edit
        ensureEmpPositionMap();
        if ("employee_add.fxml".equals(centerFxml)) initAddEmployeeForm();
        if ("employee_edit.fxml".equals(centerFxml)) initEditEmployeeForm();
    }

    private final java.util.Map<String, Long> empPositionSalary = new java.util.HashMap<>();

    private void ensureEmpPositionMap() {
        if (!empPositionSalary.isEmpty()) return;
        empPositionSalary.put("Quản lý", 10000000L);
        empPositionSalary.put("Phục vụ bàn", 5000000L);
        empPositionSalary.put("Thu Ngân", 10000000L);
        empPositionSalary.put("Bảo vệ", 5000000L);
        empPositionSalary.put("Pha chế", 7000000L);
    }

    private void initAddEmployeeForm() {
        try {
            if (addPosition != null) {
                addPosition.getItems().setAll(empPositionSalary.keySet());
                if (addPosition.getItems().size() > 0 && addPosition.getValue() == null) {
                    addPosition.setValue(addPosition.getItems().get(0));
                    addSalary.setText(formatSalary(empPositionSalary.get(addPosition.getValue())));
                }
                addPosition.setOnAction(e -> {
                    String sel = addPosition.getValue();
                    Long s = empPositionSalary.getOrDefault(sel, 0L);
                    if (addSalary != null) addSalary.setText(formatSalary(s));
                });
            }
        } catch (Exception ignored) {}
    }

    private void initEditEmployeeForm() {
        try {
            // populate position list
            if (editPosition != null) {
                editPosition.getItems().setAll(empPositionSalary.keySet());
            }

            // if user selected an employee in the list, prefill form
            EmployeeForm selected = (employeeTable == null) ? null : employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (editFullName != null) editFullName.setText(selected.getFullName());
                if (editAddress != null) editAddress.setText(selected.getAddress());
                if (editPhone != null) editPhone.setText(selected.getPhone());
                if (editUsername != null) editUsername.setText(selected.getUsername());
                if (editPosition != null) {
                    editPosition.setValue(selected.getPosition());
                    // update salary according to mapping (fallback to stored salary)
                    long s = empPositionSalary.getOrDefault(selected.getPosition(), selected.getSalary());
                    if (editSalary != null) editSalary.setText(formatSalary(s));
                } else {
                    if (editSalary != null) editSalary.setText(formatSalary(selected.getSalary()));
                }
                if (editPassword != null) editPassword.setText("");
            } else {
                // no selection: just initialize edit form position list
                    if (editPosition != null && editPosition.getItems().size() > 0 && editPosition.getValue() == null) {
                    editPosition.setValue(editPosition.getItems().get(0));
                    if (editSalary != null) editSalary.setText(formatSalary(empPositionSalary.get(editPosition.getValue())));
                }
            }

            if (editPosition != null) {
                editPosition.setOnAction(e -> {
                    String sel = editPosition.getValue();
                    Long s = empPositionSalary.getOrDefault(sel, 0L);
                    if (editSalary != null) editSalary.setText(formatSalary(s));
                });
            }
        } catch (Exception ignored) {}
    }

    @FXML private javafx.scene.control.TableView<EmployeeForm> employeeTable;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> colName;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> colPosition;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> colSalary;

    private void bindEmployeeTable() {
        if (employeeTable == null) return;
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPosition()));
        colSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(c.getValue().getSalary())));
        employeeTable.setItems(EmployeeStore.getEmployees());
    }

    private String formatSalary(long vnd) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("vi-VN"));
        return nf.format(vnd);
    }

    @FXML private javafx.scene.control.TableView<EmployeeForm> deleteTable;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> delColName;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> delColPosition;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> delColSalary;

    private void bindDeleteTable() {
        if (deleteTable == null) return;
        delColName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        delColPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPosition()));
        delColSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(c.getValue().getSalary())));
        deleteTable.setItems(EmployeeStore.getEmployees());

        deleteTable.setRowFactory(tv -> {
            final javafx.scene.control.TableRow<EmployeeForm> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty()) {
                    EmployeeForm selected = row.getItem();
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
        EmployeeForm selected = deleteTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EmployeeStore.getEmployees().remove(selected);
        }
        empShowDelete();
    }

    @FXML private javafx.scene.control.TextField searchField;
    @FXML private javafx.scene.control.TableView<EmployeeForm> searchTable;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> sColName;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> sColPosition;
    @FXML private javafx.scene.control.TableColumn<EmployeeForm, String> sColSalary;

    private void bindSearchTable(javafx.collections.ObservableList<EmployeeForm> data) {
        if (searchTable == null) return;
        sColName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        sColPosition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPosition()));
        sColSalary.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(formatSalary(c.getValue().getSalary())));
        searchTable.setItems(data);
    }

    @FXML
    private void empSearchSubmit() throws IOException {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        javafx.collections.ObservableList<EmployeeForm> filtered = EmployeeStore.getEmployees().filtered(e -> e.getFullName().toLowerCase().contains(query));
        bindSearchTable(filtered);
    }

    @FXML private javafx.scene.control.TextField addFullName, addAddress, addSalary, addPhone, addUsername;
    @FXML private javafx.scene.control.ComboBox<String> addPosition;
    @FXML private javafx.scene.control.PasswordField addPassword;
    @FXML private javafx.scene.control.TextField editFullName, editAddress, editSalary, editPhone, editUsername;
    @FXML private javafx.scene.control.ComboBox<String> editPosition;
    @FXML private javafx.scene.control.PasswordField editPassword;

    @FXML
    private void empAddSubmit() throws IOException {
        String name = text(addFullName);
        String position = addPosition == null ? "" : addPosition.getValue();
        long salary = parseLong(addSalary == null ? "0" : addSalary.getText());
        String phone = text(addPhone);
        String username = text(addUsername);
        String password = addPassword == null ? "" : addPassword.getText();
        String address = text(addAddress);
        if (!name.isEmpty() && !position.isEmpty() && salary > 0) {
            EmployeeStore.getEmployees().add(new EmployeeForm(name, address, position, salary, phone, username, password));
            empShowList();
        }
    }

    @FXML
    private void empEditSubmit() throws IOException {
        if (employeeTable != null) {
            EmployeeForm selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setFullName(text(editFullName));
                selected.setAddress(text(editAddress));
                String pos = editPosition == null ? selected.getPosition() : editPosition.getValue();
                selected.setPosition(pos);
                selected.setSalary(parseLong(editSalary == null ? String.valueOf(selected.getSalary()) : editSalary.getText()));
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
