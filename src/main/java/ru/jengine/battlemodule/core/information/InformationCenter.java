package ru.jengine.battlemodule.core.information;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.personalinfo.BattleModelInfo;
import ru.jengine.utils.ServiceLocator;

public interface InformationCenter extends ServiceLocator<InformationService> {
    void initialize(BattleContext battleContext);
    BattleModelInfo getPersonalInfoOnTurn(int personalId);
}
