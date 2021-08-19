package ru.jengine.battlemodule.models;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.serviceclasses.Direction;

public interface HasDirection {
    @Nullable
    Direction getDirection();
    void setDirection(Direction direction);
}
