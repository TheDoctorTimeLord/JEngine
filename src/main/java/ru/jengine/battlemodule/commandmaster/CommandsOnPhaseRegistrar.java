package ru.jengine.battlemodule.commandmaster;

import ru.jengine.battlemodule.commands.BattleCommandPerformElement;

public interface CommandsOnPhaseRegistrar {
    void registerCommandOnNextPhase(BattleCommandPerformElement battleCommand);
}
