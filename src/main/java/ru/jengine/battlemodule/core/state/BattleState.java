package ru.jengine.battlemodule.core.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.serviceclasses.Point;

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
        BattleModel model = battleModelById.get(id);

        if (model == null) {
            throw new BattleException("Battle model with id [" + id + "] was not found");
        }

        return model;
    }

    public Map<Point, List<Integer>> getMapFilling() {
        return battleModelOnField;
    }

    public List<Integer> getOnPosition(Point point) {
        List<Integer> models = battleModelOnField.get(point);

        if (models != null && models.isEmpty()) {
            battleModelOnField.remove(point);
        }
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(models);
    }

    private List<Integer> getOnPositionEditable(Point point) {
        return battleModelOnField.computeIfAbsent(point, p -> new ArrayList<>());
    }

    public void removeFromPosition(Point position, int id) {
        List<Integer> onCell = battleModelOnField.get(position);

        if (onCell == null) {
            throw new BattleException("Battle model with id [" + id + "] can not stay on [" + position + "]");
        }

        onCell.remove((Integer)id);

        if (onCell.isEmpty()) {
            battleModelOnField.remove(position);
        }
    }

    public void setToPosition(Point position, int id) {
        getOnPositionEditable(position).add(id);
    }

    public List<BattleModel> getDynamicObjects() {
        return dynamicObjects.stream()
                .map(this::resolveId)
                .collect(Collectors.toList());
    }

    public List<Integer> getDynamicObjectIds() {
        return new ArrayList<>(dynamicObjects);
    }

    public void addDynamicObject(BattleModel battleModel) {
        int id = battleModel.getId();

        dynamicObjects.add(id);
        battleModelById.put(id, battleModel);

        Point position;
        if (battleModel instanceof HasPosition && (position = ((HasPosition)battleModel).getPosition()) != null) {
            setToPosition(position, id);
        }
    }

    public void removeDynamicObject(int id) {
        dynamicObjects.remove((Integer)id);
        BattleModel model = battleModelById.remove(id);

        if (model instanceof HasPosition) {
            HasPosition hasPosition = (HasPosition)model;
            Point position = hasPosition.getPosition();
            if (position != null) {
                removeFromPosition(position, id);
            }
        }
    }
}
