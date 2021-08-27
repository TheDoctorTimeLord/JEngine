package ru.jengine.battlemodule.standartfilling.movement;

import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.eventqueue.event.Event;

public class MoveEvent implements Event {
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
