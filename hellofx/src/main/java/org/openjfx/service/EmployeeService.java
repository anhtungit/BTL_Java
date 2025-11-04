package org.openjfx.service;

import org.openjfx.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployee();
    Employee getEmployeeByAccountID(int id);
    Employee getEmployeeById(int id);
    void save(Employee employee);
}
