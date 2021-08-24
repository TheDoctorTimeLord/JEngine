package ru.jengine.battlemodule.information;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.information.informaionservices.InformationService;
import ru.jengine.battlemodule.information.personalinfo.BattleModelInfo;
import ru.jengine.utils.ServiceLocator;

public interface InformationCenter extends ServiceLocator<InformationService> {
    void initializeByBattleContext(BattleContext battleContext);
    BattleModelInfo getPersonalInfoOnTurn(int personalId);
}
