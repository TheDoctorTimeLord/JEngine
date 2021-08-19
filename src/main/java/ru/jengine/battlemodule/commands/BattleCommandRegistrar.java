package ru.jengine.battlemodule.commands;

import java.util.Collection;

public interface BattleCommandRegistrar {
    void registerCommand(BattleCommand battleCommand);
    Collection<BattleCommand> getAllCommands();
}
