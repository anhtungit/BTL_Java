package org.openjfx.Models;

public class EmployeeForm {
    private int employeeID;
    private int positionID;
    private int accountID;

    private String fullName;
    private String address;
    private String position;
    private long salary;
    private String phone;
    private String username;
    private String password;

    public EmployeeForm() {
    }

    public EmployeeForm(String fullName, String address, String position, long salary, String phone, String username, String password) {
        this.fullName = fullName;
        this.address = address;
        this.position = position;
        this.salary = salary;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    public EmployeeForm(int employeeID, int positionID, int accountID, String fullName, String address, String position, long salary, String phone, String username, String password) {
        this.employeeID = employeeID;
        this.positionID = positionID;
        this.accountID = accountID;
        this.fullName = fullName;
        this.address = address;
        this.position = position;
        this.salary = salary;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


