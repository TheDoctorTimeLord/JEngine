package ru.jengine.battlemodule.commands;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.models.BattleModel;
import ru.jengine.beancontainer.service.HasPriority;

public interface BattleCommand<P extends CommandExecutionParameters> extends HasPriority {
    P createParametersTemplate(Integer modelId, BattleContext battleContext);
    boolean canExecute(BattleModel model, BattleContext battleContext);
    boolean isAvailableCommand(BattleModel model, BattleContext battleContext);
    void perform(BattleModel model, BattleContext battleContext, P executionParameters);
}
