package ru.jengine.battlemodule.core.behaviors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.models.BattleModel;

import com.google.common.collect.Multimap;

public interface BehaviorObjectsManager {
    void bindBehaviors(List<BattleModel> allDynamicObjects, InformationCenter informationCenter);
    void unbindBehavior(int dynamicObjectId);
    Collection<BattleCommandPerformElement<?>> extractNewCommands(Map<Integer, List<BattleCommand<?>>> availableCommands);
    Collection<BattleCommandPerformElement<?>> handleAdditionalCommands(Multimap<Integer, AdditionalBattleCommand<?>> additionalCommands);
}
