package ru.jengine.battlemodule.standardfilling.dynamicmodel;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.BattleModelType;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;

/**
 * Класс, описывающий динамический объект в бою. Динамический объект характеризуется тем, что он:
 * <ol>
 * <li>Может осуществлять действия</li>
 * <li>Находится на некоторой позиции поля боя</li>
 * <li>Имеет некоторое направление взгляда</li>
 * </ol>
 */
public class DynamicModel extends BattleModel implements CanMoved {
    private Direction direction;
    private Point position;

    public DynamicModel(int id, BattleModelType type) {
        this(id, type, new AttributesContainer());
    }

    public DynamicModel(int id, BattleModelType type, AttributesContainer attributesContainer) {
        super(id, type, attributesContainer);
    }

    @Override
    public boolean hasDirection() {
        return direction != null;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean hasPosition() {
        return position != null;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    @Nullable
    public Point nextPosition() {
        return hasDirection() && hasPosition() ? getPosition().add(getDirection().getOffset()) : null;
    }
}
