package org.openjfx.service;

import org.openjfx.entity.Unit;

import java.util.List;

public interface UnitService {
    Unit getUnitByUnitID(int unitID);
    Unit getUnitByUnitName(String unitName);
    List<Unit> getAllUnit();
}
