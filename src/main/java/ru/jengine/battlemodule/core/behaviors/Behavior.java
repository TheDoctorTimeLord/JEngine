package ru.jengine.battlemodule.core.behaviors;

import java.util.List;
import java.util.Set;

import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.models.BattleModel;

public interface Behavior {
    Set<Integer> bind(List<BattleModel> dynamicObjects, InformationCenter informationCenter);
    void unbind(int unboundedId);
    BattleCommandPerformElement<?> sendAction(int characterId, List<BattleCommand<?>> availableCommands);
    BattleCommandPerformElement<?> handleAdditionalCommand(int characterId, AdditionalBattleCommand<?> command);
}
