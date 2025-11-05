package org.openjfx.service;

import org.openjfx.entity.Table;

import java.util.List;

public interface TableService {
    List<Table> getAllTables();
    void changeStatusTable(Table table);
    void save(Table table);
    Table getTableByTableID(int tableID);
    Table getTableByTableName(String tableName);
}
