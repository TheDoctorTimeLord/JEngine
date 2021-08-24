package ru.jengine.battlemodule.core.commandmaster;

import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;

public interface CommandsOnPhaseRegistrar {
    void registerCommandOnNextPhase(BattleCommandPerformElement<?> battleCommand);
}
