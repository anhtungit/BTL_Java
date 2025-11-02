package org.openjfx.entity;

public class Table {
    private int tableID;
    private String status;
    private String tableName;

    
    public Table() {
    }


    public Table(int tableID, String status, String tableName) {
        this.tableID = tableID;
        this.status = status;
        this.tableName = tableName;
    }


    public int getTableID() {
        return tableID;
    }


    public void setTableID(int tableID) {
        this.tableID = tableID;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getTableName() {
        return tableName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    
}
