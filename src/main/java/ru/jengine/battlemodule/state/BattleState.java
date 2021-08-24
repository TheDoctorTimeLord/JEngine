package ru.jengine.battlemodule.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public BattleModel resolveId(int id) {
        return battleModelById.get(id); //TODO бросать ошибку, если null
    }

    public Map<Point, List<Integer>> getBattleModelOnField() {
        return battleModelOnField;
    }

    public List<BattleModel> getDynamicObjects() {
        return dynamicObjects.stream()
                .map(this::resolveId)
                .collect(Collectors.toList());
    }

    public List<Integer> getDynamicObjectIds() {
        return new ArrayList<>(dynamicObjects);
    }
}
