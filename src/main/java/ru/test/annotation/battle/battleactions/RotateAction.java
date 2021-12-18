package ru.test.annotation.battle.battleactions;

import ru.jengine.battlemodule.core.battlepresenter.BattleAction;
import ru.jengine.battlemodule.core.serviceclasses.Direction;

public class RotateAction implements BattleAction {
    private final int characterId;
    private final Direction rotateTo;

    public RotateAction(int characterId, Direction rotateTo) {
        this.characterId = characterId;
        this.rotateTo = rotateTo;
    }

    public int getCharacterId() {
        return characterId;
    }

    public Direction getRotateTo() {
        return rotateTo;
    }

    @Override
    public String toString() {
        return "Rotate {" +
                "characterId=" + characterId +
                ", rotateTo=" + rotateTo +
                '}';
    }
}
