package ru.jengine.battlemodule.core.models;

import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;

public interface BattleModelType extends ObjectType {
    AttributesContainer getAttributes();
    BattleModel createBattleModelByType(int modelId);
}
