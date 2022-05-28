package ru.jengine.battlemodule.core.state;

import java.util.HashSet;
import java.util.Set;

import ru.jengine.battlemodule.core.serviceclasses.Point;

public class BattlefieldLimitersManager {
    private BattlefieldLimiter[] battlefieldLimiters;

    public void setBattlefieldLimiters(BattlefieldLimiter[] battlefieldLimiters) {
        this.battlefieldLimiters = battlefieldLimiters;
    }

    /**
     * Возвращает все клетки в пределах поля боя.
     */
    public Set<Point> getBattlefieldCells() {
        Set<Point> battlefield = new HashSet<>();

        for (BattlefieldLimiter limiter : battlefieldLimiters) {
            battlefield.addAll(limiter.getPointsInBound());
        }

        return battlefield;
    }

    /**
     * Проверяет, находится ли точка в пределах поля боя
     * @param point проверяемая точка
     * @return true - если точка находится в пределах поля боя, false - иначе
     */
    public boolean inBattlefieldBound(Point point) {
        if (battlefieldLimiters.length == 0) {
            return true;
        }

        for (BattlefieldLimiter limiter : battlefieldLimiters) {
            if (limiter.inBound(point)) {
                return true;
            }
        }
        return false;
    }
}
