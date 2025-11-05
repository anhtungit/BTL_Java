package org.openjfx.service;

import org.openjfx.entity.Unit;

import java.util.List;

public interface UnitService {
    Unit getUnitByUnitID(int unitID);
    List<Unit> getAllUnit();
}
