package ru.jengine.battlemodule.standartfilling.model;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public interface CanMoved extends HasDirection {
    boolean hasPosition();
    Point getPosition();
    void setPosition(Point position);

    Point nextPosition();

    @Nullable
    static CanMoved castToCanMoved(BattleModel model) {
        if (!(model instanceof CanMoved)) {
            return null;
        }
        CanMoved canMoved = (CanMoved)model;
        return canMoved.hasDirection() && canMoved.hasPosition() ? canMoved : null;
    }
}
