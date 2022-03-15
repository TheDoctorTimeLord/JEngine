package ru.jengine.battlemodule.core.models;

import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;

/**
 * Базовый класс для объекта в бою
 */
public abstract class BattleModel {
    private final int id;
    private final AttributesContainer attributes = new AttributesContainer();

    protected BattleModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Метод получения контейнера, содержащего все атрибуты объекта
     */
    public AttributesContainer getAttributes() {
        return attributes;
    }
}
