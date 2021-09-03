package ru.jengine.battlemodule.standardfilling.model;

import ru.jengine.battlemodule.core.serviceclasses.Direction;

public interface HasDirection {
    boolean hasDirection();
    Direction getDirection();
    void setDirection(Direction direction);
}
