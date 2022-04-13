package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRule;

/**
 * Условие, описывающее может ли некоторый
 * {@link AttributeRule обработчик
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
