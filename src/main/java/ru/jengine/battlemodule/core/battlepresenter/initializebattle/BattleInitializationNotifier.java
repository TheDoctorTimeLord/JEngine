package ru.jengine.battlemodule.core.battlepresenter.initializebattle;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.battlepresenter.BattleActionRegistrar;

/**
 * Специальный объект, который инициирует уведомление подписчиков на начальное состояние боя, о том, какие действия
 * произошли. Объект задуман для того, чтобы можно было иметь отложенное уведомление о начальном состоянии, поскольку
 * непосредственно после инициализации {@link ru.jengine.battlemodule.core.BattleMaster BattleMaster'а} подписчики
 * ещё не успели подписаться на какие-либо события в бою
 */
@BattleBeanPrototype
public class BattleInitializationNotifier {
    private final List<InitializationPresenter> presenters;

    public BattleInitializationNotifier(List<InitializationPresenter> presenters) {
        this.presenters = presenters;
    }

    /**
     * Инициирует уведомление подписчиков на начальное состояние боя, о том, какие действия произошли
     * @param battleContext контекст боя, хранящий все данные о бое
     * @param actionRegistrar регистратор действий в бою
     */
    public void notifyAboutInitialization(BattleContext battleContext, BattleActionRegistrar actionRegistrar) {
        presenters.forEach(presenter ->
                presenter.presentBattleInitialize(battleContext).forEach(actionRegistrar::registerAction)
        );
        actionRegistrar.declareEndBattleInitialization();
    }
}
