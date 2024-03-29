package ru.jengine.battlemodule.standardfilling.movement;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.standardfilling.dynamicmodel.HasDirection;

public interface CanMoved extends HasDirection, HasPosition {
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
