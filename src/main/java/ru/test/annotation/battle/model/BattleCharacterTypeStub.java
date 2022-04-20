package ru.test.annotation.battle.model;

import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.BattleModelType;

public class BattleCharacterTypeStub implements BattleModelType {
    @Override
    public AttributesContainer getAttributes() {
        return new AttributesContainer();
    }

    @Override
    public BattleModel createBattleModelByType(int modelId) {
        return new BattleCharacter(modelId, 10);
    }

    @Override
    public String getObjectTypeName() {
        return "character";
    }
}
