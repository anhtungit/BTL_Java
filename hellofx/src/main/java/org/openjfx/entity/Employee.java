package org.openjfx.entity;

public class Employee {
    private int employeeID;
    private int positionID;
    private int accountID;
    private String fullName;
    private String phoneNumber;
    private String address;

    public Employee() {
    }

    public Employee(int positionID, int accountID, String fullName, String phoneNumber, String address) {
        this.positionID = positionID;
        this.accountID = accountID;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Employee(int employeeID, int positionID, int accountID, String fullName, String phoneNumber, String address) {
        this.employeeID = employeeID;
        this.positionID = positionID;
        this.accountID = accountID;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
