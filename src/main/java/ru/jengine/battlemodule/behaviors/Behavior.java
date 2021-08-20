package ru.jengine.battlemodule.behaviors;

import java.util.List;
import java.util.Set;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.commands.BattleCommand;
import ru.jengine.battlemodule.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.models.BattleModel;

public interface Behavior {
    Set<Integer> bind(List<BattleModel> dynamicObjects, BattleContext battleContext);
    BattleCommandPerformElement<?> sendAction(int characterId, List<BattleCommand<?>> availableCommands);
}
