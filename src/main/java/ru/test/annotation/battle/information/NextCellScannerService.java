package ru.test.annotation.battle.information;

import ru.jengine.battlemodule.core.information.InformationService;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;

public class NextCellScannerService implements InformationService {
    private final BattleState battleState;

    public NextCellScannerService(BattleState battleState) {
        this.battleState = battleState;
    }

    public boolean hasEnemyInNextTwoCells(int visitorId) {
        BattleModel battleModel = battleState.resolveId(visitorId);
        CanMoved canMoved = CanMoved.castToCanMoved(battleModel);
        if (canMoved == null) {
            return false;
        }

        Direction viewDirection = canMoved.getDirection();
        Point position = canMoved.getPosition()
                .add(viewDirection.getOffset())
                .add(viewDirection.getOffset());

        Point nextPosition = position
                .add(viewDirection.getOffset());

        return !battleState.getOnPosition(position).isEmpty() || !battleState.getOnPosition(nextPosition).isEmpty();
    }

    public Point getNextCell(int visitorId)
    {
        BattleModel battleModel = battleState.resolveId(visitorId);
        CanMoved canMoved = CanMoved.castToCanMoved(battleModel);
        if (canMoved == null) {
            return null;
        }

        return canMoved.getPosition().add(canMoved.getDirection().getOffset());
    }
}
