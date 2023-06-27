package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.state.BattleState;

public class ChangeDirectionHandler extends AbstractChangeDirectionHandler<ChangeDirectionEvent> {
    public ChangeDirectionHandler(BattleState battleState) {
        super(battleState);
    }
}
