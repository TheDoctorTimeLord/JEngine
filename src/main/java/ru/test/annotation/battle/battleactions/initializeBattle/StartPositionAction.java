package ru.test.annotation.battle.battleactions.initializeBattle;

import ru.jengine.battlemodule.core.battlepresenter.BattleAction;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public class StartPositionAction implements BattleAction {
    private final Point characterPosition;
    private final Direction characterDirection;
    private final int characterId;

    public StartPositionAction(int characterId, Point characterPosition, Direction characterDirection) {
        this.characterPosition = characterPosition;
        this.characterDirection = characterDirection;
        this.characterId = characterId;
    }

    public Direction getCharacterDirection() {
        return characterDirection;
    }

    public int getCharacterId() {
        return characterId;
    }

    public Point getCharacterPosition() {
        return characterPosition;
    }

    @Override
    public String toString() {
        return "Start {" +
                "characterId=" + characterId +
                ", characterPosition=" + characterPosition +
                ", characterDirection=" + characterDirection +
                '}';
    }
}
