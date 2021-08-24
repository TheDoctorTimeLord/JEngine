package ru.jengine.battlemodule.information.informaionservices;

import java.util.List;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.information.InformationCenter;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;

@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public class InformationRegistrarService {
    private final List<InformationServiceRegistrar> registrars;

    public InformationRegistrarService(List<InformationServiceRegistrar> registrars) {
        this.registrars = registrars;
    }

    public void registerInformationServices(InformationCenter informationCenter, BattleContext context) {
        registrars.forEach(registrar -> registrar.registerInformationService(informationCenter, context));
    }
}
