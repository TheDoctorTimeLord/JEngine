package ru.jengine.battlemodule.information.informaionservices;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.information.InformationCenter;

public interface InformationServiceRegistrar {
    void registerInformationService(InformationCenter informationCenter, BattleContext battleContext);
}
