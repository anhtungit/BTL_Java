package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Employee;
import org.openjfx.service.EmployeeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public List<Employee> getAllEmployee() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT EmployeeID, PositionID, AccountID, FullName, PhoneNumber, Address
                    FROM Employee
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setPositionID(rs.getInt("PositionID"));
                employee.setAccountID(rs.getInt("AccountID"));
                employee.setFullName(rs.getString("FullName"));
                employee.setPhoneNumber(rs.getString("PhoneNumber"));
                employee.setAddress(rs.getString("Address"));
                employees.add(employee);
            }
        } catch (SQLException e) {
        }
        return employees;
    }

    @Override
    public Employee getEmployeeByAccountID(int accountId) {
        Employee employee = new Employee();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT EmployeeID, PositionID, FullName, PhoneNumber, Address
                    FROM Employee
                    WHERE AccountID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setPositionID(rs.getInt("PositionID"));
                employee.setAccountID(accountId);
                employee.setFullName(rs.getString("FullName"));
                employee.setPhoneNumber(rs.getString("PhoneNumber"));
                employee.setAddress(rs.getString("Address"));
            }
        } catch (SQLException e) {
        }
        return employee;
    }

    @Override
    public Employee getEmployeeById(int id) {
        Employee employee = new Employee();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT PositionID, AccountID, FullName, PhoneNumber, Address
                    FROM Employee
                    WHERE EmployeeID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                employee.setEmployeeID(id);
                employee.setPositionID(rs.getInt("PositionID"));
                employee.setAccountID(rs.getInt("AccountID"));
                employee.setFullName(rs.getString("FullName"));
                employee.setPhoneNumber(rs.getString("PhoneNumber"));
                employee.setAddress(rs.getString("Address"));
            }
        } catch (SQLException e) {
        }
        return employee;
    }

    @Override
    public void save(Employee employee) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE Employee SET FullName = ?, Address = ?, PhoneNumber = ?, PositionID = ? WHERE EmployeeID = ?";

            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, employee.getFullName());
            ps.setString(2, employee.getAddress());
            ps.setString(3, employee.getPhoneNumber());
            ps.setInt(4, employee.getPositionID());
            ps.setInt(5, employee.getEmployeeID());

            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }
}
