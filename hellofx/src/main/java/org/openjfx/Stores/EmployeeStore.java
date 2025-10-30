package org.openjfx.Stores;

import org.openjfx.Models.Employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeStore {
    private static final ObservableList<Employee> employees = FXCollections.observableArrayList(
        new Employee("Đặng Văn Dũng", "123 Nguyễn Trãi, Đà Nẵng", "Quản lý", 10000000L, "0957654763", "admin1", "123456"),
        new Employee("Lê Văn Thể", "12 Ngô Quyền, Đà Nẵng", "Phục vụ bàn", 5000000L, "0945423984", "vanthe14", "123456"),
        new Employee("Trần Thị Giang", "45 Lê Lợi, Đà Nẵng", "Thu Ngân", 10000000L, "0912345678", "giangtt", "123456"),
        new Employee("Võ Bình Yên", "78 Trần Phú, Đà Nẵng", "Bảo vệ", 5000000L, "0987654321", "yenvb", "123456")
    );

    public static ObservableList<Employee> getEmployees() {
        return employees;
    }
}
