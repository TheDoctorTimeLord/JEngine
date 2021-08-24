package ru.jengine.battlemodule.behaviors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.commands.BattleCommand;
import ru.jengine.battlemodule.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.information.InformationCenter;
import ru.jengine.battlemodule.models.BattleModel;

public interface BehaviorObjectsManager {
    void bindBehaviors(List<BattleModel> allDynamicObjects, InformationCenter informationCenter);
    Collection<BattleCommandPerformElement<?>> extractNewCommands(Map<Integer, List<BattleCommand<?>>> availableCommands);
}
