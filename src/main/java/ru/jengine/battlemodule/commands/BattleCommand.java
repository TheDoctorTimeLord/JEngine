package ru.jengine.battlemodule.commands;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.models.BattleModel;
import ru.jengine.beancontainer.service.HasPriority;

public interface BattleCommand extends HasPriority {
    boolean canExecute(BattleModel model, BattleContext commandContext);
    boolean isValid(BattleModel model, BattleContext context);
    void perform(BattleModel model, BattleContext context);
}
