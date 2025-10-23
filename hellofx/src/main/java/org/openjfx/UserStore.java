package org.openjfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserStore {
    private static UserStore instance;
    
    private StringProperty fullName = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty position = new SimpleStringProperty();
    private StringProperty salary = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    private UserStore() {
        // Default values
        fullName.set("Lê Văn Thể");
        address.set("12 Ngô Quyền, tp Đà Nẵng");
        phone.set("0945423984");
        username.set("vanthe14");
        position.set("Nhân viên pha chế");
        salary.set("5.000.000");
        password.set("123456");
    }

    public static UserStore getInstance() {
        if (instance == null) {
            instance = new UserStore();
        }
        return instance;
    }

    // Getters and setters with property access
    public String getFullName() { return fullName.get(); }
    public void setFullName(String value) { fullName.set(value); }
    public StringProperty fullNameProperty() { return fullName; }

    public String getAddress() { return address.get(); }
    public void setAddress(String value) { address.set(value); }
    public StringProperty addressProperty() { return address; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String value) { phone.set(value); }
    public StringProperty phoneProperty() { return phone; }

    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }
    public StringProperty usernameProperty() { return username; }

    public String getPosition() { return position.get(); }
    public void setPosition(String value) { position.set(value); }
    public StringProperty positionProperty() { return position; }

    public String getSalary() { return salary.get(); }
    public void setSalary(String value) { salary.set(value); }
    public StringProperty salaryProperty() { return salary; }

    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }
}