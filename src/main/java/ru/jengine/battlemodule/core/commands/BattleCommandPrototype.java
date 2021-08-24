package ru.jengine.battlemodule.core.commands;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.models.BattleModel;

public interface BattleCommandPrototype<P extends CommandExecutionParameters, C extends BattleCommand<P>> {
    boolean canExecute(BattleModel model, BattleContext battleContext);
    boolean isAvailableCommand(BattleModel model, BattleContext battleContext);
    C createBattleCommand(BattleModel model, BattleContext battleContext);
}
