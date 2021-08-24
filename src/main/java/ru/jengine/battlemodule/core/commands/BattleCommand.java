package ru.jengine.battlemodule.core.commands;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.beancontainer.service.HasPriority;

public interface BattleCommand<P extends CommandExecutionParameters> extends HasPriority {
    P createParametersTemplate();
    void perform(BattleModel model, BattleContext battleContext, P executionParameters);
}
