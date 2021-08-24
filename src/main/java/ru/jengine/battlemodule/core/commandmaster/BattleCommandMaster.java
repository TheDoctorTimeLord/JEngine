package ru.jengine.battlemodule.core.commandmaster;

import ru.jengine.battlemodule.core.BattleContext;

public interface BattleCommandMaster extends CommandsOnPhaseRegistrar {
    void takeTurn(BattleContext commandContext);
}
