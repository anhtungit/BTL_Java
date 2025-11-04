package org.openjfx.service;

import org.openjfx.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployee();
    Employee getEmployeeByAccountID(int id);
    Employee getEmployeeById(int id);
    List<Employee> getEmployeeByName(String name);
    void save(Employee employee);
    void delete(Employee employee);
    void create(Employee employee);

}
