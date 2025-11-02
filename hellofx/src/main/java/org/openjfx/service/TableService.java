package org.openjfx.service;

import org.openjfx.entity.Table;

import java.util.List;

public interface TableService {
    List<Table> getAllTables();
    void changeStatusByTable(Table table);
    void save(Table table);
}
