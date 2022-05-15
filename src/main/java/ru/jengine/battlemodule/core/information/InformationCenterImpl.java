package ru.jengine.battlemodule.core.information;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;

@BattleBeanPrototype
public class InformationCenterImpl implements InformationCenter {
    private final Map<Class<? extends InformationService>, InformationService> services = new ConcurrentHashMap<>();

    @Override
    public <S extends InformationService> void setService(Class<S> serviceType, S service) {
        services.put(serviceType, service);
    }

    @Override
    public <S extends InformationService> S getService(Class<S> serviceType) {
        return (S) services.get(serviceType);
    }

    @Override
    public boolean containsService(Class<? extends InformationService> serviceType) {
        return services.containsKey(serviceType);
    }
}
