package ru.jengine.battlemodule.behaviors;

import java.util.List;
import java.util.Set;

import ru.jengine.battlemodule.commands.BattleCommand;
import ru.jengine.battlemodule.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.information.InformationCenter;
import ru.jengine.battlemodule.models.BattleModel;

public interface Behavior {
    Set<Integer> bind(List<BattleModel> dynamicObjects, InformationCenter informationCenter);
    BattleCommandPerformElement<?> sendAction(int characterId, List<BattleCommand<?>> availableCommands);
}
