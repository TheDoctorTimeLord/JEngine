package ru.jengine.battlemodule.core.commandmaster;

import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;

public interface CommandsOnPhaseRegistrar {
    void registerCommandOnNextPhase(int battleModelId, AdditionalBattleCommand<?> battleCommand);
}
