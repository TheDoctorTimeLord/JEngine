package ru.jengine.battlemodule.core.battlepresenter.initializebattle;

import java.util.List;

import ru.jengine.battlemodule.core.battlepresenter.BattleAction;

public interface InitializationPresenter {
    List<BattleAction> presentBattleInitialize(InitializationNotifierContext notifierContext);
}
