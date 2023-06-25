package ru.jengine.battlemodule.core.commandmaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.scheduler.SchedulerTaskExecutor;
import ru.jengine.battlemodule.core.state.BattleDynamicObjectsManager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Реализация интерфейса {@link BattleCommandMaster}
 */
@BattleBeanPrototype
public class BattleCommandMasterImpl implements BattleCommandMaster {
    private final List<BattleCommandPerformElement<?>> commandsOnNextPhase = new ArrayList<>();
    private final Multimap<Integer, AdditionalBattleCommand<?>> registeredCommandOnNextPhase = HashMultimap.create();

    @Override
    public void takeTurn(BattleContext battleContext, SchedulerTaskExecutor taskExecutor) {
        BattleDynamicObjectsManager dynamicObjectsManager = battleContext.getBattleDynamicObjectsManager();

        List<BattleCommandPerformElement<?>> commandOnPhase =
                sortCommandByPriority(dynamicObjectsManager.extractCommandOnTurn(battleContext));

        taskExecutor.executeBeforeTurn();

        while (!commandOnPhase.isEmpty()) {
            performAllCommands(commandOnPhase, battleContext);
            clarifyCommandParameters(dynamicObjectsManager);

            taskExecutor.executeAfterPhase();
            commandOnPhase = getCommandsOnNextPhase();
        }

        taskExecutor.executeAfterTurn();
    }

    @Override
    public void registerCommandOnNextPhase(int battleModelId, AdditionalBattleCommand<?> battleCommand) {
        synchronized (registeredCommandOnNextPhase) {
            registeredCommandOnNextPhase.put(battleModelId, battleCommand);
        }
    }

    private void clarifyCommandParameters(BattleDynamicObjectsManager dynamicObjectsManager) {
        Multimap<Integer, AdditionalBattleCommand<?>> registeredCommands;

        synchronized (registeredCommandOnNextPhase) {
            registeredCommands = HashMultimap.create(registeredCommandOnNextPhase);
            registeredCommandOnNextPhase.clear();
        }

        if (registeredCommands.isEmpty()) {
            return;
        }

        commandsOnNextPhase.addAll(dynamicObjectsManager.handleAdditionalCommands(registeredCommands));
    }

    private List<BattleCommandPerformElement<?>> getCommandsOnNextPhase() {
        List<BattleCommandPerformElement<?>> result = sortCommandByPriority(commandsOnNextPhase);
        commandsOnNextPhase.clear();
        return result;
    }

    private void performAllCommands(List<BattleCommandPerformElement<?>> commands, BattleContext commandContext) {
        for (BattleCommandPerformElement<?> command : commands) {
            try {
                command.performCommand(commandContext);
            } catch (Exception e) {
                logger.error("BattleCommandMasterImpl", "Command [%s] performs with error".formatted(command), e);
            }
        }
    }

    private static List<BattleCommandPerformElement<?>> sortCommandByPriority(
            Collection<BattleCommandPerformElement<?>> commands)
    {
        return commands.stream()
                .sorted(Comparator.comparingInt(element -> element.getBattleCommand().getPriority()))
                .collect(Collectors.toList());
    }
}
