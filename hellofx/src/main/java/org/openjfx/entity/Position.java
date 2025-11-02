package org.openjfx.entity;

public class Position {
    private int positionId;
    private int salary;
    private String positionName;
    public Position() {
    }
    public Position(int positionId, int salary, String positionName) {
        this.positionId = positionId;
        this.salary = salary;
        this.positionName = positionName;
    }
    public int getPositionId() {
        return positionId;
    }
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public String getPositionName() {
        return positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    
    
}
