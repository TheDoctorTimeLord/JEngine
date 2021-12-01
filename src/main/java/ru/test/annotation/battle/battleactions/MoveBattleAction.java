package ru.test.annotation.battle.battleactions;

import ru.jengine.battlemodule.core.battlepresenter.BattleAction;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public class MoveBattleAction implements BattleAction {
    private final int characterId;
    private final Point oldPosition;
    private final Point newPosition;

    public MoveBattleAction(int characterId, Point oldPosition,
            Point newPosition) {
        this.characterId = characterId;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    public int getCharacterId() {
        return characterId;
    }

    public Point getNewPosition() {
        return newPosition;
    }

    public Point getOldPosition() {
        return oldPosition;
    }

    @Override
    public String toString() {
        return "Move {" +
                "character=" + characterId +
                ", from=" + oldPosition +
                ", to=" + newPosition +
                '}';
    }
}
