package ru.jengine.battlemodule.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.models.BattleModel;
import ru.jengine.battlemodule.models.CanMoved;
import ru.jengine.battlemodule.serviceclasses.Point;

public class BattleMapService {
    private final BattleState battleState;
    private final BattleObjectsManager objectsManager;

    public BattleMapService(BattleState battleState, BattleObjectsManager objectsManager) {
        this.battleState = battleState;
        this.objectsManager = objectsManager;
    }

    public void changePosition(Point from, Point to, int id) {
        BattleModel model = objectsManager.resolve(id);

        if (!(model instanceof CanMoved)) {
            return; //TODO залогировать
        }
        CanMoved moved = (CanMoved)model;

        if (moved.getPosition() == null || moved.getDirection() == null) {
            return; //TODO залогировать
        }

        moved.setPosition(to);
        removeFromPosition(from, model.getId());
        setToPosition(to, model.getId());
    }

    private void removeFromPosition(Point position, int id) {
        Map<Point, List<Integer>> map = battleState.getBattleModelOnField();
        List<Integer> onCell = map.get(position);
        onCell.remove((Integer)id);

        if (onCell.isEmpty()) {
            map.remove(position);
        }
    }

    private void setToPosition(Point position, int id) {
        battleState.getBattleModelOnField()
                .computeIfAbsent(position, p -> new ArrayList<>())
                .add(id);
    }

    public List<Integer> getOnPosition(Point position) {
        return battleState.getBattleModelOnField().getOrDefault(position, Collections.emptyList());
    }
}
