package org.openjfx.service;

import org.openjfx.entity.Position;

import java.util.List;

public interface PositionService {
    Position getPositionByPositionID(int id);
    Position getPositionByPositionName(String positionName);
    List<Position> getAllPosition();

}
