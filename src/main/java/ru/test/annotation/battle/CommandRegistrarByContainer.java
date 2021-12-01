package ru.test.annotation.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPrototype;
import ru.jengine.battlemodule.core.commands.BattleCommandRegistrar;
import ru.jengine.battlemodule.core.commands.CommandExecutionParameters;
import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class CommandRegistrarByContainer implements BattleCommandRegistrar {
    private final List<BattleCommandPrototype<?, ?>> allBattleCommands;

    public CommandRegistrarByContainer(List<BattleCommandPrototype<?, ?>> allBattleCommands) {
        this.allBattleCommands = allBattleCommands;
    }

    @Override
    public <P extends CommandExecutionParameters, C extends BattleCommand<P>> void registerCommand(
            BattleCommandPrototype<P, C> battleCommandPrototype)
    {
        allBattleCommands.add(battleCommandPrototype);
    }

    @Override
    public Collection<BattleCommandPrototype<?, ?>> getAllCommands() {
        return new ArrayList<>(allBattleCommands);
    }
}
