package ru.jengine.battlemodule.core.models;

import ru.jengine.battlemodule.core.serviceclasses.Point;

/**
 * Интерфейс для объекта, который может быть расположен на поле боя
 */
public interface HasPosition {
    /**
     * Возвращает true, если объект находится на некоторой клетке поля боя
     */
    boolean hasPosition();

    /**
     * Возвращает координаты клетки поля боя, на которой находится объект
     */
    Point getPosition();

    /**
     * Устанавливает координаты клетки поля боя, на которой находится объект
     * @param position координаты клетки поля боя
     */
    void setPosition(Point position);

    /**
     * Осуществляет каст объекта в бою, к объекту, который может находиться на некоторой клетке поле боя
     * @param model объект в бою
     * @return объект, прикастованный к {@link HasPosition}, либо null, такой каст невозможен
     */
    static HasPosition castToHasPosition(BattleModel model) {
        if (!(model instanceof HasPosition)) {
            return null;
        }
        HasPosition castedModel = (HasPosition)model;
        return castedModel.hasPosition() ? castedModel : null;
    }
}
