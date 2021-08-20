package ru.jengine.battlemodule.commands;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.models.BattleModel;
import ru.jengine.beancontainer.service.HasPriority;

public interface BattleCommand<P extends CommandExecutionParameters> extends HasPriority {
    P createParametersTemplate();
    void perform(BattleModel model, BattleContext battleContext, P executionParameters);
}
