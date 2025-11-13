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
        Employee selected = null;
        
        // Check if employee is selected from employee list table
        if (employeeTable != null && employeeTable.getSelectionModel().getSelectedItem() != null) {
            selected = employeeTable.getSelectionModel().getSelectedItem();
        } 
        // Check if employee is selected from search table
        else if (searchTable != null && searchTable.getSelectionModel().getSelectedItem() != null) {
            selected = searchTable.getSelectionModel().getSelectedItem();
        }
        
        if (selected == null) {
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
                    int s = positionService.getPositionByPositionName(addPosition.getValue()).getSalary();
                    if (addSalary != null) addSalary.setText(formatSalary(s));
                });
            }
            
            // Add phone number input restriction - only digits
            if (addPhone != null) {
                addPhone.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal.matches("\\d*")) {
                        addPhone.setText(newVal.replaceAll("[^\\d]", ""));
                    } else if (newVal.length() > 10) {
                        addPhone.setText(newVal.substring(0, 10));
                    }
                });
            }
            
            // Add real-time validation listeners
            if (addFullName != null) {
                addFullName.textProperty().addListener((obs, oldVal, newVal) -> {
                    validateFullNameField(newVal, addFullNameError);
                });
            }
            
            if (addPhone != null) {
                addPhone.textProperty().addListener((obs, oldVal, newVal) -> {
                    validatePhoneField(newVal, addPhoneError);
                });
            }
            
            if (addUsername != null) {
                addUsername.textProperty().addListener((obs, oldVal, newVal) -> {
                    validateUsernameField(newVal, addUsernameError);
                });
            }
            
            if (addPassword != null) {
                addPassword.textProperty().addListener((obs, oldVal, newVal) -> {
                    validatePasswordField(newVal, addPasswordError);
                });
            }
            
            if (addPosition != null) {
                addPosition.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.isEmpty()) {
                        if (addPositionError != null) addPositionError.setText("");
                    }
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

            // Get selected employee from employeeTable or searchTable
            Employee selected = null;
            if (employeeTable != null && employeeTable.getSelectionModel().getSelectedItem() != null) {
                selected = employeeTable.getSelectionModel().getSelectedItem();
            } else if (searchTable != null && searchTable.getSelectionModel().getSelectedItem() != null) {
                selected = searchTable.getSelectionModel().getSelectedItem();
            }
            
            if (selected == null) return;
            
            editFullName.setText(selected.getFullName());
            editAddress.setText(selected.getAddress());
            editPhone.setText(selected.getPhoneNumber());
            editUsername.setText(accountService.getAccountByAccountID(selected.getAccountID()).getUsername());
            editPosition.setValue(positionService.getPositionByPositionID(selected.getPositionID()).getPositionName());
            editSalary.setText(formatSalary(positionService.getPositionByPositionID(selected.getPositionID()).getSalary()));
            editPassword.setText("");

            if (editPosition != null) {
                editPosition.setOnAction(e -> {
                    if (editSalary != null) editSalary.setText(formatSalary(positionService.getPositionByPositionName(editPosition.getValue()).getSalary()));
                });
            }
            
            // Add phone number input restriction - only digits
            if (editPhone != null) {
                editPhone.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal.matches("\\d*")) {
                        editPhone.setText(newVal.replaceAll("[^\\d]", ""));
                    } else if (newVal.length() > 10) {
                        editPhone.setText(newVal.substring(0, 10));
                    }
                });
            }
            
            // Add real-time validation listeners
            if (editFullName != null) {
                editFullName.textProperty().addListener((obs, oldVal, newVal) -> {
                    validateFullNameField(newVal, editFullNameError);
                });
            }
            
            if (editPhone != null) {
                editPhone.textProperty().addListener((obs, oldVal, newVal) -> {
                    validatePhoneField(newVal, editPhoneError);
                });
            }
            
            if (editPassword != null) {
                editPassword.textProperty().addListener((obs, oldVal, newVal) -> {
                    validatePasswordFieldOptional(newVal, editPasswordError);
                });
            }
            
            if (editPosition != null) {
                editPosition.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.isEmpty()) {
                        if (editPositionError != null) editPositionError.setText("");
                    }
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
                        bindDeleteTable();
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
        java.util.List<Employee> employees = employeeService.getEmployeeByName(query);
        bindSearchTable(FXCollections.observableArrayList(employees));
    }

    @FXML private javafx.scene.control.TextField addFullName, addAddress, addSalary, addPhone, addUsername;
    @FXML private javafx.scene.control.ComboBox<String> addPosition;
    @FXML private javafx.scene.control.PasswordField addPassword;
    @FXML private javafx.scene.control.Label addFullNameError, addPositionError, addPhoneError, addUsernameError, addPasswordError;
    @FXML private javafx.scene.control.TextField editFullName, editAddress, editPhone;
    @FXML private javafx.scene.control.Label editSalary, editUsername;
    @FXML private javafx.scene.control.ComboBox<String> editPosition;
    @FXML private javafx.scene.control.PasswordField editPassword;
    @FXML private javafx.scene.control.Label editFullNameError, editPositionError, editPhoneError, editPasswordError;

    //Todo: Xong
    @FXML
    private void empAddSubmit() throws IOException {
        String name = text(addFullName).trim();
        String position = addPosition == null ? "" : addPosition.getValue();
        String phone = text(addPhone).trim();
        String username = text(addUsername).trim();
        String password = addPassword == null ? "" : addPassword.getText();
        String address = text(addAddress).trim();
        
        // Validation
        if (!validateEmployeeAddInput(name, position, phone, username, password, address)) {
            return;
        }
        
        int accountID = accountService.create(new Account(username, password));
        int positionID = positionService.getPositionByPositionName(position).getPositionId();
        employeeService.create(new Employee(positionID, accountID, name, phone, address));
        empShowList();
    }

    @FXML
    private void empEditSubmit() throws IOException {
        Employee selected = null;
        
        // Get selected employee from employeeTable or searchTable
        if (employeeTable != null && employeeTable.getSelectionModel().getSelectedItem() != null) {
            selected = employeeTable.getSelectionModel().getSelectedItem();
        } else if (searchTable != null && searchTable.getSelectionModel().getSelectedItem() != null) {
            selected = searchTable.getSelectionModel().getSelectedItem();
        }
        
        if (selected != null) {
            Account accSeleted = accountService.getAccountByAccountID(selected.getAccountID());
            
            String name = text(editFullName).trim();
            String phone = text(editPhone).trim();
            String position = editPosition == null ? "" : editPosition.getValue();
            String password = editPassword == null ? "" : editPassword.getText();
            String address = text(editAddress).trim();
            
            // Validation
            if (!validateEmployeeEditInput(name, position, phone, password, address)) {
                return;
            }
            
            selected.setFullName(name);
            selected.setAddress(address);
            if (editPosition != null)
                selected.setPositionID(positionService.getPositionByPositionName(editPosition.getValue()).getPositionId());
            selected.setPhoneNumber(phone);

            if (!password.isEmpty()) {
                accSeleted.setPassword(password);
                accountService.save(accSeleted);
            }
            employeeService.save(selected);
            showAlert("Thành công", "Chỉnh sửa nhân viên thành công!");
        }
        empShowList();
    }

    @FXML
    private void empBackToList() throws IOException {
        empShowList();
    }

    private String text(javafx.scene.control.TextField tf) { return tf == null ? "" : tf.getText(); }

    // Validation methods
    private boolean validateEmployeeAddInput(String name, String position, String phone, String username, String password, String address) {
        // Validate name
        if (name.isEmpty()) {
            showAlert("Lỗi", "Họ và tên không được để trống!");
            return false;
        }
        if (name.length() < 3) {
            showAlert("Lỗi", "Họ và tên phải có ít nhất 3 ký tự!");
            return false;
        }
        if (name.length() > 100) {
            showAlert("Lỗi", "Họ và tên không được vượt quá 100 ký tự!");
            return false;
        }
        if (!name.matches("^[a-zA-ZÀ-ỿ\\s]+$")) {
            showAlert("Lỗi", "Họ và tên chỉ được chứa chữ cái và khoảng trắng!");
            return false;
        }
        
        // Validate position
        if (position.isEmpty()) {
            showAlert("Lỗi", "Chức vụ không được để trống!");
            return false;
        }
        
        // Validate phone
        if (phone.isEmpty()) {
            showAlert("Lỗi", "Số điện thoại không được để trống!");
            return false;
        }
        if (!phone.matches("^\\d{10}$")) {
            showAlert("Lỗi", "Số điện thoại phải đúng 10 chữ số!");
            return false;
        }
        
        // Validate username
        if (username.isEmpty()) {
            showAlert("Lỗi", "Tên đăng nhập không được để trống!");
            return false;
        }
        if (username.length() < 4) {
            showAlert("Lỗi", "Tên đăng nhập phải có ít nhất 4 ký tự!");
            return false;
        }
        if (username.length() > 50) {
            showAlert("Lỗi", "Tên đăng nhập không được vượt quá 50 ký tự!");
            return false;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            showAlert("Lỗi", "Tên đăng nhập chỉ được chứa chữ cái, số và gạch dưới!");
            return false;
        }
        if (accountService.userNameIsPresent(username)) {
            showAlert("Lỗi", "Tên đăng nhập đã có người sử dụng. Nhập tên đăng nhập khác!");
            return false;
        }
        
        // Validate password
        if (password.isEmpty()) {
            showAlert("Lỗi", "Mật khẩu không được để trống!");
            return false;
        }
        if (password.length() < 6) {
            showAlert("Lỗi", "Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }
        if (password.length() > 50) {
            showAlert("Lỗi", "Mật khẩu không được vượt quá 50 ký tự!");
            return false;
        }
        
        // Validate address (optional but if provided, validate)
        if (!address.isEmpty() && address.length() > 200) {
            showAlert("Lỗi", "Địa chỉ không được vượt quá 200 ký tự!");
            return false;
        }
        
        return true;
    }
    
    private boolean validateEmployeeEditInput(String name, String position, String phone, String password, String address) {
        // Validate name
        if (name.isEmpty()) {
            showAlert("Lỗi", "Họ và tên không được để trống!");
            return false;
        }
        if (name.length() < 3) {
            showAlert("Lỗi", "Họ và tên phải có ít nhất 3 ký tự!");
            return false;
        }
        if (name.length() > 100) {
            showAlert("Lỗi", "Họ và tên không được vượt quá 100 ký tự!");
            return false;
        }
        if (!name.matches("^[a-zA-ZÀ-ỿ\\s]+$")) {
            showAlert("Lỗi", "Họ và tên chỉ được chứa chữ cái và khoảng trắng!");
            return false;
        }
        
        // Validate position
        if (position.isEmpty()) {
            showAlert("Lỗi", "Chức vụ không được để trống!");
            return false;
        }
        
        // Validate phone
        if (phone.isEmpty()) {
            showAlert("Lỗi", "Số điện thoại không được để trống!");
            return false;
        }
        if (!phone.matches("^\\d{10}$")) {
            showAlert("Lỗi", "Số điện thoại phải đúng 10 chữ số!");
            return false;
        }
        
        // Validate password (optional if not changing password)
        if (!password.isEmpty()) {
            if (password.length() < 6) {
                showAlert("Lỗi", "Mật khẩu phải có ít nhất 6 ký tự!");
                return false;
            }
            if (password.length() > 50) {
                showAlert("Lỗi", "Mật khẩu không được vượt quá 50 ký tự!");
                return false;
            }
        }
        
        // Validate address (optional but if provided, validate)
        if (!address.isEmpty() && address.length() > 200) {
            showAlert("Lỗi", "Địa chỉ không được vượt quá 200 ký tự!");
            return false;
        }
        
        return true;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Real-time validation helper methods
    private void validateFullNameField(String value, javafx.scene.control.Label errorLabel) {
        if (errorLabel == null) return;
        
        value = value.trim();
        if (value.isEmpty()) {
            errorLabel.setText("Họ và tên không được để trống!");
            return;
        }
        if (value.length() < 3) {
            errorLabel.setText("Họ và tên phải có ít nhất 3 ký tự!");
            return;
        }
        if (value.length() > 100) {
            errorLabel.setText("Họ và tên không được vượt quá 100 ký tự!");
            return;
        }
        if (!value.matches("^[a-zA-ZÀ-ỿ\\s]+$")) {
            errorLabel.setText("Họ và tên chỉ được chứa chữ cái!");
            return;
        }
        errorLabel.setText("");
    }
    
    private void validatePhoneField(String value, javafx.scene.control.Label errorLabel) {
        if (errorLabel == null) return;
        
        value = value.trim();
        if (value.isEmpty()) {
            errorLabel.setText("Số điện thoại không được để trống!");
            return;
        }
        if (!value.matches("^\\d*$")) {
            errorLabel.setText("Số điện thoại chỉ được chứa chữ số!");
            return;
        }
        if (value.length() > 0 && value.length() < 10) {
            errorLabel.setText("Số điện thoại phải đúng 10 chữ số!");
            return;
        }
        errorLabel.setText("");
    }
    
    private void validateUsernameField(String value, javafx.scene.control.Label errorLabel) {
        if (errorLabel == null) return;
        
        value = value.trim();
        if (value.isEmpty()) {
            errorLabel.setText("Tên đăng nhập không được để trống!");
            return;
        }
        if (value.length() < 4) {
            errorLabel.setText("Tên đăng nhập phải có ít nhất 4 ký tự!");
            return;
        }
        if (value.length() > 50) {
            errorLabel.setText("Tên đăng nhập không được vượt quá 50 ký tự!");
            return;
        }
        if (!value.matches("^[a-zA-Z0-9_]+$")) {
            errorLabel.setText("Chỉ được dùng chữ, số và gạch dưới!");
            return;
        }
        if (accountService.userNameIsPresent(value)) {
            errorLabel.setText("Tên đăng nhập này đã được sử dụng!");
            return;
        }
        errorLabel.setText("");
    }
    
    private void validatePasswordField(String value, javafx.scene.control.Label errorLabel) {
        if (errorLabel == null) return;
        
        if (value.isEmpty()) {
            errorLabel.setText("Mật khẩu không được để trống!");
            return;
        }
        if (value.length() < 6) {
            errorLabel.setText("Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }
        if (value.length() > 50) {
            errorLabel.setText("Mật khẩu không được vượt quá 50 ký tự!");
            return;
        }
        errorLabel.setText("");
    }
    
    private void validatePasswordFieldOptional(String value, javafx.scene.control.Label errorLabel) {
        if (errorLabel == null) return;
        
        if (value.isEmpty()) {
            errorLabel.setText("");
            return;
        }
        if (value.length() < 6) {
            errorLabel.setText("Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }
        if (value.length() > 50) {
            errorLabel.setText("Mật khẩu không được vượt quá 50 ký tự!");
            return;
        }
        errorLabel.setText("");
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
