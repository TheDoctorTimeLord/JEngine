package ru.jengine.battlemodule.core.models;

/**
 * Базовый класс для объекта в бою
 */
public abstract class BattleModel {
    private final int id;

    protected BattleModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
