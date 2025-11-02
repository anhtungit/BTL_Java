package org.openjfx.Stores;

import org.openjfx.Models.EmployeeForm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeStore {
    private static final ObservableList<EmployeeForm> EMPLOYEE_FORMS = FXCollections.observableArrayList(
        new EmployeeForm("Đặng Văn Dũng", "123 Nguyễn Trãi, Đà Nẵng", "Quản lý", 10000000L, "0957654763", "admin1", "123456"),
        new EmployeeForm("Lê Văn Thể", "12 Ngô Quyền, Đà Nẵng", "Phục vụ bàn", 5000000L, "0945423984", "vanthe14", "123456"),
        new EmployeeForm("Trần Thị Giang", "45 Lê Lợi, Đà Nẵng", "Thu Ngân", 10000000L, "0912345678", "giangtt", "123456"),
        new EmployeeForm("Võ Bình Yên", "78 Trần Phú, Đà Nẵng", "Bảo vệ", 5000000L, "0987654321", "yenvb", "123456")
    );

    public static ObservableList<EmployeeForm> getEmployees() {
        return EMPLOYEE_FORMS;
    }
}
