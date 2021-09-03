package ru.jengine.battlemodule.standardfilling.movement;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public class MoveEvent extends BattleEvent {
    private final Point oldPosition;
    private final Point newPosition;
    private final int modelId;

    public MoveEvent(int modelId, Point oldPosition, Point newPosition) {
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.modelId = modelId;
    }

    public int getModelId() {
        return modelId;
    }

    public Point getNewPosition() {
        return newPosition;
    }

    public Point getOldPosition() {
        return oldPosition;
    }
}
