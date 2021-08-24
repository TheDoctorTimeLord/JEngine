package ru.jengine.battlemodule.core.information;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.informaionservices.InformationService;
import ru.jengine.battlemodule.core.information.personalinfo.BattleModelInfo;
import ru.jengine.utils.ServiceLocator;

public interface InformationCenter extends ServiceLocator<InformationService> {
    void initializeByBattleContext(BattleContext battleContext);
    BattleModelInfo getPersonalInfoOnTurn(int personalId);
}
