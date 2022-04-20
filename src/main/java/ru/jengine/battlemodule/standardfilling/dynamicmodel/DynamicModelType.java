package ru.jengine.battlemodule.standardfilling.dynamicmodel;

import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.models.BattleModelType;

public class DynamicModelType implements BattleModelType {
    private final String name;
    private final AttributesContainer attributesContainer;

    public DynamicModelType(String name, AttributesContainer attributesContainer) {
        this.name = name;
        this.attributesContainer = attributesContainer;
    }

    @Override
    public String getObjectTypeName() {
        return name;
    }

    @Override
    public AttributesContainer getAttributes() {
        return attributesContainer;
    }

    @Override
    public DynamicModel createBattleModelByType(int modelId) {
        return new DynamicModel(modelId, this, attributesContainer.clone());
    }
}
