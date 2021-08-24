package ru.jengine.battlemodule.core.information.informaionservices;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.InformationCenter;

@BattleBeanPrototype
public class InformationRegistrarService {
    private final List<InformationServiceRegistrar> registrars;

    public InformationRegistrarService(List<InformationServiceRegistrar> registrars) {
        this.registrars = registrars;
    }

    public void registerInformationServices(InformationCenter informationCenter, BattleContext context) {
        registrars.forEach(registrar -> registrar.registerInformationService(informationCenter, context));
    }
}
