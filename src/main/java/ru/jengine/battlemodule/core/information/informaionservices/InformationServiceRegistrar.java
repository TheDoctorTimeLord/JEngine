package ru.jengine.battlemodule.core.information.informaionservices;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.InformationCenter;

public interface InformationServiceRegistrar {
    void registerInformationService(InformationCenter informationCenter, BattleContext battleContext);
}
