package org.openjfx;

public class Employee {
    private int id;
    private String name;
    private String position;
    private double salary;
    private String phone;
    private String email;
    private String address;
    private String username;
    private String password;
    private String role;

    public Employee(String name, String position, double salary, String phone, String email, String password) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Constructor cho database
    public Employee(String name, String position, double salary, String phone, String email, String address,
            boolean isDatabase) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return name; // Backward compatibility
    }

    public void setFullName(String name) {
        this.name = name; // Backward compatibility
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
