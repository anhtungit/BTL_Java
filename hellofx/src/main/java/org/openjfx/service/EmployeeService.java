package org.openjfx.service;

import org.openjfx.entity.Employee;

public interface EmployeeService {
    Employee getEmployeeByAccountID(int id);
    Employee getEmployeeById(int id);
    void save(Employee employee);
}
