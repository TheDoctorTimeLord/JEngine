package ru.jengine.battlemodule.models;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.serviceclasses.Point;

public interface CanMoved extends HasDirection {
    boolean hasPosition();
    Point getPosition();
    void setPosition(Point position);

    @Nullable
    static CanMoved castToCanMoved(BattleModel model) {
        if (!(model instanceof CanMoved)) {
            return null;
        }
        CanMoved canMoved = (CanMoved)model;
        return canMoved.hasDirection() && canMoved.hasPosition() ? canMoved : null;
    }
}
