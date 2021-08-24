package ru.jengine.battlemodule.core.commands;

import java.util.Collection;

public interface BattleCommandRegistrar {
    <P extends CommandExecutionParameters, C extends BattleCommand<P>> void registerCommand(
            BattleCommandPrototype<P, C> battleCommandPrototype);

    Collection<BattleCommandPrototype<?, ?>> getAllCommands();
}
