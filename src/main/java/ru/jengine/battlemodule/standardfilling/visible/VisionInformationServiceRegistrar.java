package ru.jengine.battlemodule.standardfilling.visible;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.events.EventHandlerBinder;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.information.informaionservices.InformationServiceRegistrar;
import ru.jengine.battlemodule.standardfilling.visible.outside.BaseSectorScanner;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestriction;
import ru.jengine.battlemodule.standardfilling.visible.outside.TileClassifier;

@BattleBeanPrototype
public class VisionInformationServiceRegistrar implements InformationServiceRegistrar, EventHandlerBinder {
    private final CustomRowRestriction customRowRestriction;
    private final TileClassifier tileClassifier;
    private final BaseSectorScanner baseSectorScanner;
    private volatile UpdatableVisionInformationService service;
    private RecalculateAfterMoveHandler handler;

    public VisionInformationServiceRegistrar(TileClassifier tileClassifier, CustomRowRestriction customRowRestriction,
            BaseSectorScanner baseSectorScanner)
    {
        this.tileClassifier = tileClassifier;
        this.customRowRestriction = customRowRestriction;
        this.baseSectorScanner = baseSectorScanner;
    }

    @Override
    public void bindPostHandlerToEvent(BattleContext battleContext) {
        battleContext.getDispatcher().registerPostHandler(handler =
                new RecalculateAfterMoveHandler(getService(battleContext)));
    }

    @Override
    public void unbindPostHandlerToEvent(DispatcherBattleWrapper dispatcher) {
        dispatcher.removePostHandler(handler);
    }

    @Override
    public void registerInformationService(InformationCenter informationCenter, BattleContext battleContext) {
        UpdatableVisionInformationService service = getService(battleContext);

        battleContext.getBattleDynamicObjectsManager().getAllCharacters().stream()
                .filter(battleModel -> HasVision.castToHasVision(battleModel) != null)
                .forEach(battleModel -> service.recalculateFieldOfView(battleModel.getId()));

        informationCenter.setService(VisionInformationService.class, service);
    }

    private UpdatableVisionInformationService getService(BattleContext battleContext) {
        if (service == null) {
            synchronized (VisionInformationServiceRegistrar.class) {
                if (service == null) {
                    service = new VisionInformationServiceImpl(battleContext.getBattleState(), customRowRestriction,
                            tileClassifier, baseSectorScanner);
                }
            }
        }
        return service;
    }
}
