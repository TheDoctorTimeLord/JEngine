package ru.jengine.battlemodule.core.battlepresenter.initializebattle;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.InformationCenter;

public class InitializationNotifierContext {
    private final BattleContext battleContext;
    private final InformationCenter informationCenter;

    public InitializationNotifierContext(BattleContext battleContext, InformationCenter informationCenter) {
        this.battleContext = battleContext;
        this.informationCenter = informationCenter;
    }

    public BattleContext getBattleContext() {
        return battleContext;
    }

    public InformationCenter getInformationCenter() {
        return informationCenter;
    }
}
