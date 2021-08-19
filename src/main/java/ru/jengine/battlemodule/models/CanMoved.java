package ru.jengine.battlemodule.models;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.serviceclasses.Point;

public interface CanMoved extends HasDirection {
    @Nullable
    Point getPosition();
    void setPosition(Point position);
}
