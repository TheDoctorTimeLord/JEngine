package ru.test.annotation.battle.events;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.serviceclasses.Direction;

public class TestChangeDirectionEvent extends BattleEvent {
    private final int modelId;
    private final Direction direction;

    public TestChangeDirectionEvent(int modelId, @Nullable Direction direction) {
        this.modelId = modelId;
        this.direction = direction;
    }

    public int getModelId() {
        return modelId;
    }

    @Nullable
    public Direction getNewDirection() {
        return direction;
    }
}
