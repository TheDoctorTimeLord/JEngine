package ru.jengine.battlemodule.commandmaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.state.BattleDynamicObjectsManager;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;

@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public class BattleCommandMasterImpl implements BattleCommandMaster {
    private final List<BattleCommandPerformElement> commandsOnNextPhase = new ArrayList<>();

    @Override
    public void takeTurn(BattleContext commandContext) {
        BattleDynamicObjectsManager dynamicObjectsManager = commandContext.getBattleDynamicObjectsManager();
        List<BattleCommandPerformElement> commandOnPhase =
                sortCommandByPriority(dynamicObjectsManager.extractCommandOnTurn(commandContext));

        while (!commandOnPhase.isEmpty()) {
            performAllCommands(commandOnPhase, commandContext);

            commandOnPhase = getCommandsOnNextPhase();
        }
    }

    @Override
    public void registerCommandOnNextPhase(BattleCommandPerformElement battleCommand) {
        synchronized (commandsOnNextPhase) {
            commandsOnNextPhase.add(battleCommand);
        }
    }

    private List<BattleCommandPerformElement> getCommandsOnNextPhase() {
        synchronized (commandsOnNextPhase) {
            List<BattleCommandPerformElement> result = sortCommandByPriority(commandsOnNextPhase);
            commandsOnNextPhase.clear();
            return result;
        }
    }

    private static void performAllCommands(List<BattleCommandPerformElement> commands, BattleContext commandContext) {
        for (BattleCommandPerformElement command : commands) {
            command.performCommand(commandContext);
        }
    }

    private static List<BattleCommandPerformElement> sortCommandByPriority(Collection<BattleCommandPerformElement> commands) {
        return commands.stream()
                .sorted(Comparator.comparingInt(element -> element.getBattleCommand().getPriority()))
                .collect(Collectors.toList());
    }
}
