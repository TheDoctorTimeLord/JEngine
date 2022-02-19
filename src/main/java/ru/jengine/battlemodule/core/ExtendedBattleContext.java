package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.information.InformationCenter;

/**
 * Расширенный контекст боя. Дополнительно был включён {@link InformationCenter}
 * //TODO не забыть поменять, если {@link InformationCenter} будет включен в основной контекст
 */
public class ExtendedBattleContext {
    private final BattleContext battleContext;
    private final InformationCenter informationCenter;

    public ExtendedBattleContext(BattleContext battleContext, InformationCenter informationCenter) {
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
