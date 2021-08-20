package ru.jengine.battlemodule.commands;

import java.util.Collection;

public interface BattleCommandRegistrar {
    <P extends CommandExecutionParameters> void registerCommand(BattleCommand<P> battleCommand);
    Collection<BattleCommand<?>> getAllCommands();
}
