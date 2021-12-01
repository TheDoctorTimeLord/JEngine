package ru.test.annotation.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.behaviors.Behavior;
import ru.jengine.battlemodule.core.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.models.BattleModel;

import com.google.common.collect.Multimap;

@BattleBeanPrototype
public class SynchronousBehaviorObjectManager implements BehaviorObjectsManager {
    private final List<Behavior> behaviors;
    private final Map<Integer, Behavior> bindings = new HashMap<>();

    public SynchronousBehaviorObjectManager(List<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    @Override
    public void bindBehaviors(List<BattleModel> allDynamicObjects, InformationCenter informationCenter) {
        List<BattleModel> currentModels = new ArrayList<>(allDynamicObjects);
        
        for (Behavior behavior : behaviors) {
            if (currentModels.isEmpty()) {
                break;
            }

            Set<Integer> bound = behavior.bind(currentModels, informationCenter);

            List<BattleModel> newCurrentModels = new ArrayList<>();
            currentModels.forEach(model -> {
                if (bound.contains(model.getId())) {
                    bindings.put(model.getId(), behavior);
                } else {
                    newCurrentModels.add(model);
                }
            });
            currentModels = newCurrentModels;
        }
    }

    @Override
    public void unbindBehavior(int dynamicObjectId) {
        Behavior binding = bindings.remove(dynamicObjectId);
        if (binding != null) {
            binding.unbind(dynamicObjectId);
        }
    }

    @Override
    public Collection<BattleCommandPerformElement<?>> extractNewCommands(
            Map<Integer, List<BattleCommand<?>>> availableCommands)
    {
        Collection<BattleCommandPerformElement<?>> result = new ArrayList<>();

        availableCommands.forEach((id, commands) -> {
            if (!bindings.containsKey(id)) {
                return;
            }

            Behavior behavior = bindings.get(id);
            result.add(behavior.sendAction(id, commands));
        });

        return result;
    }

    @Override
    public Collection<BattleCommandPerformElement<?>> handleAdditionalCommands(
            Multimap<Integer, AdditionalBattleCommand<?>> additionalCommands)
    {
        Collection<BattleCommandPerformElement<?>> result = new ArrayList<>();

        additionalCommands.asMap().forEach((id, commands) -> commands.forEach(command ->
                        result.add(behaviors.get(id).handleAdditionalCommand(id, command))
                )
        );

        return result;
    }
}
