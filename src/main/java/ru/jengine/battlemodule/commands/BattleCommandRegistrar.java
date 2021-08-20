package ru.jengine.battlemodule.commands;

import java.util.Collection;

public interface BattleCommandRegistrar {
    <P extends CommandExecutionParameters, C extends BattleCommand<P>> void registerCommand(
            BattleCommandPrototype<P, C> battleCommandPrototype);

    Collection<BattleCommandPrototype<?, ?>> getAllCommands();
}
