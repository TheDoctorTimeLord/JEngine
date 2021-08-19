package ru.jengine.battlemodule.commandmaster;

import ru.jengine.battlemodule.BattleContext;

public interface BattleCommandMaster extends CommandsOnPhaseRegistrar {
    void takeTurn(BattleContext commandContext);
}
