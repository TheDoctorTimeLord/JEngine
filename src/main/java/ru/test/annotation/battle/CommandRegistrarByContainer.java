package ru.test.annotation.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandFactory;
import ru.jengine.battlemodule.core.commands.BattleCommandRegistrar;
import ru.jengine.battlemodule.core.commands.CommandExecutionParameters;
import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class CommandRegistrarByContainer implements BattleCommandRegistrar {
    private final List<BattleCommandFactory<?, ?>> allBattleCommands;

    public CommandRegistrarByContainer(List<BattleCommandFactory<?, ?>> allBattleCommands) {
        this.allBattleCommands = allBattleCommands;
    }

    @Override
    public <P extends CommandExecutionParameters, C extends BattleCommand<P>> void registerCommand(
            BattleCommandFactory<P, C> battleCommandFactory)
    {
        allBattleCommands.add(battleCommandFactory);
    }

    @Override
    public Collection<BattleCommandFactory<?, ?>> getAllCommands() {
        return new ArrayList<>(allBattleCommands);
    }
}
