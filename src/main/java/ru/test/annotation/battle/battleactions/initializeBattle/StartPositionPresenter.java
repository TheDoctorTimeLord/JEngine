package ru.test.annotation.battle.battleactions.initializeBattle;

import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.battlepresenter.BattleAction;
import ru.jengine.battlemodule.core.ExtendedBattleContext;
import ru.jengine.battlemodule.core.battlepresenter.initializebattle.InitializationPresenter;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.model.DynamicModel;

@BattleBeanPrototype
public class StartPositionPresenter implements InitializationPresenter {
    @Override
    public List<BattleAction> presentBattleInitialize(ExtendedBattleContext extendedBattleContext) {
        BattleState battleState = extendedBattleContext.getBattleContext().getBattleState();
        return battleState.getDynamicObjects().stream()
                .filter(model -> model instanceof DynamicModel)
                .map(model -> (DynamicModel)model)
                .filter(model -> model.hasPosition() && model.hasDirection())
                .map(model -> new StartPositionAction(model.getId(), model.getPosition(), model.getDirection()))
                .collect(Collectors.toList());
    }
}
