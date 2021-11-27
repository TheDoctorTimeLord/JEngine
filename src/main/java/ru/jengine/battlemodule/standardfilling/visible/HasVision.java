package ru.jengine.battlemodule.standardfilling.visible;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.standardfilling.model.HasDirection;

public interface HasVision extends HasDirection, HasPosition {
    boolean hasVision();
    void setVision(boolean canSee);

    @Nullable
    static HasVision castToHasVision(BattleModel model) {
        if (!(model instanceof HasVision)) {
            return null;
        }
        HasVision hasVision = (HasVision)model;
        return hasVision.hasDirection() && hasVision.hasPosition() ? hasVision : null;
    }
}
