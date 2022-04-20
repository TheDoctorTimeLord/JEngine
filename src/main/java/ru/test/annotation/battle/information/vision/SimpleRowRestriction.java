package ru.test.annotation.battle.information.vision;

import static ru.test.annotation.battle.TestBattle.MAP_SIZE;

import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestriction;
import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class SimpleRowRestriction implements CustomRowRestriction {
    @Override
    public boolean isAvailableDepth(int depth) {
        return depth < 3;
    }

    @Override
    public boolean isAvailablePoint(Point point) {
        return 0 <= point.getX() && point.getX() < MAP_SIZE && 0 <= point.getY() && point.getY() < MAP_SIZE;
    }
}
