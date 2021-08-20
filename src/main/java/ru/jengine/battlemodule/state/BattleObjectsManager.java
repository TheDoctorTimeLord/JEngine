package ru.jengine.battlemodule.state;

import ru.jengine.battlemodule.models.BattleModel;

public class BattleObjectsManager {
    private final BattleState battleState;

    public BattleObjectsManager(BattleState battleState) {
        this.battleState = battleState;
    }

    public BattleModel resolve(Integer id) {
        return battleState.getBattleModelById().get(id); //TODO бросать ошибку, если null
    }
}
