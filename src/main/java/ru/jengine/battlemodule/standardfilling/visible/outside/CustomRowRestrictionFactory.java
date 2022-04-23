package ru.jengine.battlemodule.standardfilling.visible.outside;

import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.visible.HasVision;

public interface CustomRowRestrictionFactory {
    CustomRowRestriction createRowRestriction(HasVision hasVision, BattleState battleState);
}
