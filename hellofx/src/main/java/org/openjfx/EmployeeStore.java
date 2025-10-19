package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeStore {
    private static final ObservableList<Employee> employees = FXCollections.observableArrayList(
        new Employee("Đặng Văn Dũng", "Quản lý", 10000000, "0957654763", "admin1", "123456"),
        new Employee("Lê Văn Thể", "Phục vụ bàn", 5000000, "0945423984", "vanthe14", "123456"),
        new Employee("Trần Thị Giang", "Thu Ngân", 10000000, "0912345678", "giangtt", "123456"),
        new Employee("Võ Bình Yên", "Bảo vệ", 5000000, "0987654321", "yenvb", "123456")
    );

    public static ObservableList<Employee> getEmployees() {
        return employees;
    }
}


