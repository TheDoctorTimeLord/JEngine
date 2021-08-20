package ru.jengine.battlemodule.models;

import ru.jengine.battlemodule.serviceclasses.Direction;
import ru.jengine.battlemodule.serviceclasses.Point;

public class DynamicModel extends BattleModel implements CanMoved {
    public DynamicModel(int id) {
        super(id);
    }

    @Override
    public boolean hasDirection() {
        return getProperty("direction") != null;
    }

    @Override
    public Direction getDirection() {
        return getProperty("direction");
    }

    @Override
    public void setDirection(Direction direction) {
        setProperty("direction", direction);
    }

    @Override
    public boolean hasPosition() {
        return getProperty("position") != null;
    }

    @Override
    public Point getPosition() {
        return getProperty("position");
    }

    @Override
    public void setPosition(Point position) {
        setProperty("position", position);
    }
}
