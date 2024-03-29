package ru.jengine.battlemodule.core.state;

import java.util.Set;

import ru.jengine.battlemodule.core.serviceclasses.Point;

/**
 * Описывает ограничение на размеры поля боя. Поле боя необязательно прямоугольное, оно может быть произвольной формы.
 * На одно и то же поле боя может одновременно действовать несколько ограничителей размеров, или же ни одного (тогда
 * поле боя потенциально бесконечное)
 */
public interface BattlefieldLimiter {
    /**
     * Проверяет, находится ли точка в пределах поля боя, ограничиваемого текущим ограничителем
     * @param point проверяемая точка
     * @return true - если точка удовлетворяет условиям текущего ограничителя, false - иначе
     */
    boolean inBound(Point point);

    /**
     * Возвращает все точки, находящиеся в пределах поля боя
     */
    Set<Point> getPointsInBound();
}
