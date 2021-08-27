package ru.jengine.battlemodule.standartfilling.model;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;

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

    @Override
    @Nullable
    public Point nextPosition() {
        return hasDirection() && hasPosition() ? getPosition().add(getDirection().getOffset()) : null;
    }
}
