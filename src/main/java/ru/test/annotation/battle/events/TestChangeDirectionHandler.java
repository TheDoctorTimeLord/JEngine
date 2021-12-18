package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.state.BattleState;

public class TestChangeDirectionHandler extends AbstractChangeDirectionHandler<TestChangeDirectionEvent>{
    public TestChangeDirectionHandler(BattleState battleState) {
        super(battleState);
    }

    @Override
    public Class<TestChangeDirectionEvent> getHandlingEventType() {
        return TestChangeDirectionEvent.class;
    }
}
