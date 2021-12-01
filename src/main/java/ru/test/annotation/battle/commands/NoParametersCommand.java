package ru.test.annotation.battle.commands;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPrototype;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.models.BattleModel;

public abstract class NoParametersCommand implements BattleCommand<NoneParameters>,
        BattleCommandPrototype<NoneParameters, BattleCommand<NoneParameters>>
{
    @Override
    public NoneParameters createParametersTemplate() {
        return NoneParameters.INSTANCE;
    }

    @Override
    public void perform(BattleModel model, BattleContext battleContext, NoneParameters executionParameters) {
        perform(model, battleContext);
    }

    protected abstract void perform(BattleModel model, BattleContext battleContext);
}
