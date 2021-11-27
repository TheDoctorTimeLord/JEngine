package ru.jengine.battlemodule.core.information;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.information.personalinfo.BattleModelInfo;
import ru.jengine.battlemodule.core.information.personalinfo.EditableBattleModelInfo;

@BattleBeanPrototype
public class InformationCenterImpl implements EditableInformationCenter { //TODO переделать центр информации
    private final Map<Class<? extends InformationService>, InformationService> services = new ConcurrentHashMap<>();
    private final Map<Integer, EditableBattleModelInfo> personalInfo = new ConcurrentHashMap<>();

    @Override
    public EditableBattleModelInfo getEditablePersonalInfo(int personalId) {
        EditableBattleModelInfo info = personalInfo.get(personalId);

        if (info == null) {
            throw new BattleException("Personal [" + personalId + "] has no personal info. Maybe he is not registered");
        }

        return info;
    }

    @Override
    public void initialize(BattleContext battleContext) {
        battleContext.getBattleState().getDynamicObjectIds()
                .forEach(id -> personalInfo.put(id, new EditableBattleModelInfo()));
    }

    @Override
    public BattleModelInfo getPersonalInfoOnTurn(int personalId) {
        return personalInfo.get(personalId);
    }

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
