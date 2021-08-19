package ru.jengine.battlemodule.state;

import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.models.BattleModel;
import ru.jengine.battlemodule.serviceclasses.Point;

public class BattleState {
    private final Map<Integer, BattleModel> battleModelById;
    private final Map<Point, List<Integer>> battleModelOnField;
    private final List<Integer> dynamicObjects;

    public BattleState(Map<Integer, BattleModel> battleModelById,
            Map<Point, List<Integer>> battleModelOnField, List<Integer> dynamicObjects)
    {
        this.battleModelById = battleModelById;
        this.battleModelOnField = battleModelOnField;
        this.dynamicObjects = dynamicObjects;
    }

    public Map<Integer, BattleModel> getBattleModelById() {
        return battleModelById;
    }

    public Map<Point, List<Integer>> getBattleModelOnField() {
        return battleModelOnField;
    }

    public List<Integer> getDynamicObjects() {
        return dynamicObjects;
    }
}
