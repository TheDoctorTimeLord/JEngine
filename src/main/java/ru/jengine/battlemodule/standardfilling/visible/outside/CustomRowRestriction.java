package ru.jengine.battlemodule.standardfilling.visible.outside;

import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.visible.HasVision;
import ru.jengine.utils.fieldofview.shadowcasting.RowRestriction;

public interface CustomRowRestriction extends RowRestriction {
    void initialize(HasVision hasVision, BattleState battleState);
}
