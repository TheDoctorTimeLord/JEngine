package ru.jengine.battlemodule.core.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standartfilling.model.CanMoved;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public class BattleMapService {
    private final BattleState battleState;

    public BattleMapService(BattleState battleState) {
        this.battleState = battleState;
    }

    public void changePosition(Point from, Point to, int id) {
        BattleModel model = battleState.resolveId(id);
        CanMoved moved = CanMoved.castToCanMoved(model);
        if (moved == null) {
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
