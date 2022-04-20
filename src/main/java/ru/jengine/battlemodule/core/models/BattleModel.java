package ru.jengine.battlemodule.core.models;

import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;

/**
 * Базовый класс для объекта в бою
 */
public abstract class BattleModel {
    private final int id;
    private final BattleModelType battleModelType;
    private final AttributesContainer attributes;

    protected BattleModel(int id, BattleModelType battleModelType) {
        this(id, battleModelType, new AttributesContainer());
    }

    protected BattleModel(int id, BattleModelType battleModelType, AttributesContainer attributesContainer) {
        this.id = id;
        this.battleModelType = battleModelType;
        this.attributes = attributesContainer;
    }

    public int getId() {
        return id;
    }

    public BattleModelType getBattleModelType() {
        return battleModelType;
    }

    /**
     * Метод получения контейнера, содержащего все атрибуты объекта
     */
    public AttributesContainer getAttributes() {
        return attributes;
    }
}
