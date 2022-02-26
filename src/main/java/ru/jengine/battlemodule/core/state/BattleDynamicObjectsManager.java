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
import ru.jengine.battlemodule.core.commands.BattleCommandFactory;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.models.BattleModel;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Менеджер всех объектов в бою, которые могут осуществлять какие-либо действия. Обеспечивает полное связывание между
 * динамическими игровыми объектами и их поведением. Также обрабатывает передачу доступных для выполнения команд от
 * персонажа к поведению, управляющему им.
 */
public class BattleDynamicObjectsManager {
    private final BattleState battleState;
    private final BehaviorObjectsManager behaviorObjectsManager;
    private final Map<Integer, List<BattleCommandFactory<?, ?>>> commandsForCharacter = new HashMap<>();

    public BattleDynamicObjectsManager(BattleState battleState, BehaviorObjectsManager behaviorObjectsManager) {
        this.battleState = battleState;
        this.behaviorObjectsManager = behaviorObjectsManager;
    }

    /**
     * Возвращает все динамические объекты, которые находятся в бою
     */
    public List<BattleModel> getAllCharacters() {
        return battleState.getDynamicObjects();
    }

    /**
     * Удаляет указанный динамический объект из боя и всю служебную информацию о нём
     * @param characterId ID динамического объекта
     */
    public void removeCharacter(int characterId) {
        behaviorObjectsManager.unbindBehavior(characterId);
        commandsForCharacter.remove(characterId);
        battleState.removeObject(characterId);
    }

    /**
     * Устанавливает всем персонажам команды, которые они могут выполнить в течении боя
     * @param allCommands все доступные в бою команды
     * @param battleContext контекст текущего боя
     */
    public void setCommandsForCharacters(Collection<BattleCommandFactory<?, ?>> allCommands,
            BattleContext battleContext)
    {
        for (BattleModel battleModel : getAllCharacters()) {
            List<BattleCommandFactory<?, ?>> commandList = commandsForCharacter.compute(battleModel.getId(),
                    (id, l) -> new ArrayList<>());

            allCommands.stream()
                    .filter(command -> command.canExecute(battleModel, battleContext))
                    .forEach(Objects.requireNonNull(commandList)::add);
        }
    }

    /**
     * Получает команды, которые будут совершены в новом ходу. При вызове методов происходит обработка доступных
     * действий персонажей
     * @param commandContext контекст текущего боя
     * @return команды динамических объектов в текущем ходу
     */
    public Collection<BattleCommandPerformElement<?>> extractCommandOnTurn(BattleContext commandContext) {
        BattleState battleState = commandContext.getBattleState();

        Map<Integer, List<BattleCommand<?>>> availableCommands = new HashMap<>();
        for (Map.Entry<Integer, List<BattleCommandFactory<?, ?>>> entry : commandsForCharacter.entrySet()) {
            BattleModel model = battleState.resolveId(entry.getKey());
            availableCommands.put(entry.getKey(), entry.getValue().stream()
                    .filter(command -> command.isAvailableCommand(model, commandContext))
                    .map(command -> command.createBattleCommand(model, commandContext))
                    .collect(Collectors.toList()));
        }

        return behaviorObjectsManager.extractNewCommands(availableCommands);
    }

    /**
     * Получает команды, которые должны произойти в новую фазу. Если дополнительные команды требует уточнения
     * параметров у поведения, управляющего динамическим объектом, то осуществляет такой уточнение.
     * @param registeredCommands все зарегистрированные дополнительные команды в предыдущую фазу
     * @return команды динамических объектов в новую фазу
     */
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
