package ru.test.annotation.battle.information.vision;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.visible.HasVision;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestriction;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestrictionFactory;

@BattleBeanPrototype
public class SimpleCustomRowRestrictionFactory implements CustomRowRestrictionFactory {
    @Override
    public CustomRowRestriction createRowRestriction(HasVision hasVision, BattleState battleState) {
        return new SimpleRowRestriction();
    }
}
