package ru.jengine.battlemodule.core.battlepresenter.initializebattle;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.battlepresenter.BattleActionRegistrar;

@BattleBeanPrototype
public class BattleInitializationNotifier {
    private final List<InitializationPresenter> presenters;

    public BattleInitializationNotifier(List<InitializationPresenter> presenters) {
        this.presenters = presenters;
    }

    public void notifyAboutInitialization(InitializationNotifierContext notifierContext, BattleActionRegistrar actionRegistrar) {
        presenters.forEach(presenter ->
                presenter.presentBattleInitialize(notifierContext).forEach(actionRegistrar::registerAction)
        );
        actionRegistrar.declareEndBattleInitialization();
    }
}
