package ru.jengine.battlemodule.standardfilling.visible;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.standardfilling.dynamicmodel.HasDirection;

public interface HasVision extends HasDirection, HasPosition {
    int getVisionDistance();
    boolean hasVision();
    void setVisionDistance(int visionDistance);

    @Nullable
    static HasVision castToHasVision(BattleModel model) {
        if (!(model instanceof HasVision)) {
            return null;
        }
        HasVision hasVision = (HasVision)model;
        return hasVision.hasDirection() && hasVision.hasPosition() ? hasVision : null;
    }
}
