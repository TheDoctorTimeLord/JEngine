package ru.jengine.battlemodule.core.models;

import ru.jengine.battlemodule.core.serviceclasses.Point;

public interface HasPosition {
    boolean hasPosition();
    Point getPosition();
    void setPosition(Point position);

    static HasPosition castToHasPosition(BattleModel model) {
        if (!(model instanceof HasPosition)) {
            return null;
        }
        HasPosition castedModel = (HasPosition)model;
        return castedModel.hasPosition() ? castedModel : null;
    }
}
