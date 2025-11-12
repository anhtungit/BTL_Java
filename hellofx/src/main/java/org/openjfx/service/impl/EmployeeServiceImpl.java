package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Employee;
import org.openjfx.service.EmployeeService;
import org.openjfx.service.PositionService;

import java.sql.*;
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
                if (employee.getPositionID() != 21)
                    employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT EmployeeID, PositionID, AccountID, FullName, PhoneNumber, Address
                    FROM Employee
                    WHERE FullName LIKE ?
                    """;
            String searchPattern = "%" + name + "%";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, searchPattern);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setPositionID(rs.getInt("PositionID"));
                employee.setAccountID(rs.getInt("AccountID"));
                employee.setFullName(rs.getString("FullName"));
                employee.setPhoneNumber(rs.getString("PhoneNumber"));
                employee.setAddress(rs.getString("Address"));
                if (employee.getPositionID() != 21)
                    employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
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
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Employee employee) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE Employee SET FullName = ?, Address = ?, PhoneNumber = ?, PositionID = ? WHERE EmployeeID = ?";

            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, employee.getFullName());
            ps.setString(2, employee.getAddress());
            ps.setString(3, employee.getPhoneNumber());
            ps.setInt(4, 7);
            ps.setInt(5, employee.getEmployeeID());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Employee employee) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO Employee(PositionID, AccountID, FullName, PhoneNumber, Address)
                          VALUES (?, ?, ?, ?, ?)
                          """;
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, employee.getPositionID());
            ps.setInt(2, employee.getAccountID());
            ps.setString(3, employee.getFullName());
            ps.setString(4, employee.getPhoneNumber());
            ps.setString(5, employee.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

