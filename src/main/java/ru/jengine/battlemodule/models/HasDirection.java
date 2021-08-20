package ru.jengine.battlemodule.models;

import ru.jengine.battlemodule.serviceclasses.Direction;

public interface HasDirection {
    boolean hasDirection();
    Direction getDirection();
    void setDirection(Direction direction);
}
