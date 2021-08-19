package ru.jengine.battlemodule;

import ru.jengine.battlemodule.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.commands.BattleCommandRegistrar;

public interface BattleMaster {
    void prepareBattle(BattleGenerator battleGenerator, BattleCommandRegistrar commandRegistrar,
            BehaviorObjectsManager behaviorObjectsManager);

    void takeTurn();

    void stopBattle();
}
