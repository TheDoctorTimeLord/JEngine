package ru.jengine.battlemodule.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.commands.BattleCommand;
import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.commands.BattleCommandPrototype;
import ru.jengine.battlemodule.models.BattleModel;

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
}
