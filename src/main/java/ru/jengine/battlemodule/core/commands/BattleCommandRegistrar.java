package ru.jengine.battlemodule.core.commands;

import java.util.Collection;

public interface BattleCommandRegistrar {
    <P extends CommandExecutionParameters, C extends BattleCommand<P>> void registerCommand(
            BattleCommandFactory<P, C> battleCommandFactory);

    Collection<BattleCommandFactory<?, ?>> getAllCommands();
}
