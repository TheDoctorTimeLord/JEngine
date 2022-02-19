package ru.jengine.battlemodule.core.battlepresenter;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;

/**
 * {@link ru.jengine.battlemodule.core.contentregistrars.ContentRegistrar ContentRegistrar} для корректной работой
 * отправки действий в бою
 */
@BattleBeanPrototype
public class ActionLoggerRegistrar extends AbstractContentRegistrar {
    @Override
    protected void registerInt() {
        BattleActionRegistrar actionRegistrar = battleContext.getBattleActionRegistrar();

        registerReusableTaskAfterPhase(actionRegistrar::declareNewPhase);
        registerReusableTaskAfterTurn(actionRegistrar::declareNewTurn);
    }
}
