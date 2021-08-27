package ru.jengine.battlemodule.core.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.standartfilling.model.CanMoved;

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

    public void removeDynamicObject(int id) {
        dynamicObjects.remove((Integer)id);
        BattleModel model = battleModelById.remove(id);

        if (model instanceof CanMoved) {
            CanMoved canMoved = (CanMoved)model;
            Point position = canMoved.getPosition();
            if (position != null) {
                List<Integer> onCell = battleModelOnField.get(position);
                onCell.remove((Integer)id);

                if (onCell.isEmpty()) {
                    battleModelOnField.remove(position);
                }
            }
        }
    }
}
