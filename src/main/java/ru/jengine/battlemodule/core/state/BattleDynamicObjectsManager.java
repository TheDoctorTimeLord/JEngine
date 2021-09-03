package ru.jengine.battlemodule.core.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.commands.BattleCommandPrototype;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.models.BattleModel;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class BattleDynamicObjectsManager {
    private final BattleState battleState;
    private final BehaviorObjectsManager behaviorObjectsManager;
    private final Map<Integer, List<BattleCommandPrototype<?, ?>>> commandsForCharacter = new HashMap<>();

    public BattleDynamicObjectsManager(BattleState battleState, BehaviorObjectsManager behaviorObjectsManager) {
        this.battleState = battleState;
        this.behaviorObjectsManager = behaviorObjectsManager;
    }

    public List<BattleModel> getAllCharacters() {
        return battleState.getDynamicObjects();
    }

    public void removeCharacter(int characterId) {
        behaviorObjectsManager.unbindBehavior(characterId);
        commandsForCharacter.remove(characterId);
        battleState.removeDynamicObject(characterId);
    }

    public void setCommandsForCharacters(Collection<BattleCommandPrototype<?, ?>> allCommands,
            BattleContext battleContext)
    {
        for (BattleModel battleModel : battleState.getDynamicObjects()) {
            List<BattleCommandPrototype<?, ?>> commandList = commandsForCharacter.compute(battleModel.getId(),
                    (id, l) -> new ArrayList<>());

            allCommands.stream()
                    .filter(command -> command.canExecute(battleModel, battleContext))
                    .forEach(Objects.requireNonNull(commandList)::add);
        }
    }

    public Collection<BattleCommandPerformElement<?>> extractCommandOnTurn(BattleContext commandContext) {
        BattleState battleState = commandContext.getBattleState();

        Map<Integer, List<BattleCommand<?>>> availableCommands = new HashMap<>();
        for (Map.Entry<Integer, List<BattleCommandPrototype<?, ?>>> entry : commandsForCharacter.entrySet()) {
            BattleModel model = battleState.resolveId(entry.getKey());
            availableCommands.put(entry.getKey(), entry.getValue().stream()
                    .filter(command -> command.isAvailableCommand(model, commandContext))
                    .map(command -> command.createBattleCommand(model, commandContext))
                    .collect(Collectors.toList()));
        }

        return behaviorObjectsManager.extractNewCommands(availableCommands);
    }

    public Collection<BattleCommandPerformElement<?>> handleAdditionalCommands(
            Multimap<Integer, AdditionalBattleCommand<?>> registeredCommands)
    {
        Collection<BattleCommandPerformElement<?>> commandsToPerform = new ArrayList<>();
        Multimap<Integer, AdditionalBattleCommand<?>> commandsToHandle = HashMultimap.create();

        registeredCommands.asMap().forEach((id, commands) ->
                commands.forEach(command -> {
                    if (command.createParametersTemplate() instanceof NoneParameters) {
                        AdditionalBattleCommand<NoneParameters> noneParametersCommand =
                                (AdditionalBattleCommand<NoneParameters>) command;
                        commandsToPerform.add(
                                new BattleCommandPerformElement<>(id, noneParametersCommand, NoneParameters.INSTANCE));
                    } else {
                        commandsToHandle.put(id, command);
                    }
                })
        );

        commandsToPerform.addAll(behaviorObjectsManager.handleAdditionalCommands(commandsToHandle));

        return commandsToPerform;
    }
}
