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
import ru.jengine.battlemodule.models.BattleModel;

public class BattleDynamicObjectsManager {
    private final BattleState battleState;
    private final BehaviorObjectsManager behaviorObjectsManager;
    private final Map<Integer, List<BattleCommand<?>>> commandsForCharacter = new HashMap<>();

    public BattleDynamicObjectsManager(BattleState battleState, BehaviorObjectsManager behaviorObjectsManager) {
        this.battleState = battleState;
        this.behaviorObjectsManager = behaviorObjectsManager;
    }

    public List<BattleModel> getAllCharacters(BattleContext battleContext) {
        BattleObjectsManager objectsManager = battleContext.getBattleObjectsManager();
        return battleState.getDynamicObjects().stream()
                .map(objectsManager::resolve)
                .collect(Collectors.toList());
    }

    public void setCommandsForCharacters(Collection<BattleCommand<?>> allCommands, BattleContext battleContext) {
        BattleObjectsManager objectsManager = battleContext.getBattleObjectsManager();

        for (Integer battleModelId : battleState.getDynamicObjects()) {
            BattleModel battleModel = objectsManager.resolve(battleModelId);
            List<BattleCommand<?>> commandList = commandsForCharacter.compute(battleModelId,
                    (id, l) -> new ArrayList<>());

            allCommands.stream()
                    .filter(command -> command.canExecute(battleModel, battleContext))
                    .forEach(Objects.requireNonNull(commandList)::add);
        }
    }

    public Collection<BattleCommandPerformElement<?>> extractCommandOnTurn(BattleContext commandContext) {
        BattleObjectsManager objectsManager = commandContext.getBattleObjectsManager();

        Map<Integer, List<BattleCommand<?>>> availableCommands = new HashMap<>();
        for (Map.Entry<Integer, List<BattleCommand<?>>> entry : commandsForCharacter.entrySet()) {
            BattleModel model = objectsManager.resolve(entry.getKey());
            availableCommands.put(entry.getKey(), entry.getValue().stream()
                    .filter(command -> command.isAvailableCommand(model, commandContext))
                    .collect(Collectors.toList()));
        }

        return behaviorObjectsManager.extractNewCommands(availableCommands);
    }
}
