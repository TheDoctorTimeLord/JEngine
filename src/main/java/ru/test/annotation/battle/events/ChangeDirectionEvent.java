package ru.test.annotation.battle.events;

import org.jetbrains.annotations.Nullable;

import ru.jengine.battlemodule.core.serviceclasses.Direction;

public class ChangeDirectionEvent extends TestChangeDirectionEvent {
    public ChangeDirectionEvent(int modelId, @Nullable Direction direction) {
        super(modelId, direction);
    }
}
