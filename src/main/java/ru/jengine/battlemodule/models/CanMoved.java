package ru.jengine.battlemodule.models;

import ru.jengine.battlemodule.serviceclasses.Point;

public interface CanMoved extends HasDirection {
    boolean hasPosition();
    Point getPosition();
    void setPosition(Point position);
}
