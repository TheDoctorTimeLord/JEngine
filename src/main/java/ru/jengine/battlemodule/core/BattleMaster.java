package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.battlepresenter.BattleActionPresenter;
import ru.jengine.battlemodule.core.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.core.commands.BattleCommandRegistrar;

public interface BattleMaster {
    void prepareBattle(BattleGenerator battleGenerator, BattleCommandRegistrar commandRegistrar,
            BehaviorObjectsManager behaviorObjectsManager);

    void informationAboutInitialize();

    void takeTurn();

    BattleActionPresenter getBattlePresenter();

    void stopBattle();
}
