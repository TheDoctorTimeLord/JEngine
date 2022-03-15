package ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.handlingconditions;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Условие, описывающее может ли некоторый
 * {@link ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandler обработчик
 * атрибутов} обработать изменённый атрибут
 */
public interface HandlingCondition {
    /**
     * Может ли создатель данного условия обработать изменённый атрибут
     * @param attribute изменённый атрибут
     * @return true - если может, false - в противном случае
     */
    boolean canHandle(BattleAttribute attribute);
}
