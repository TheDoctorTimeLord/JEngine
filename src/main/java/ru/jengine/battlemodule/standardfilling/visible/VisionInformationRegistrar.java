package ru.jengine.battlemodule.standardfilling.visible;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.visible.outside.BaseSectorScanner;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestrictionFactory;
import ru.jengine.battlemodule.standardfilling.visible.outside.TileClassifier;
import ru.jengine.battlemodule.standardfilling.visible.outside.VisionPostHandlerRegistrar;

@BattleBeanPrototype
public class VisionInformationRegistrar extends AbstractContentRegistrar {
    private final CustomRowRestrictionFactory customRowRestriction;
    private final TileClassifier tileClassifier;
    private final BaseSectorScanner baseSectorScanner;
    private final VisionPostHandlerRegistrar visionPostHandlerRegistrar;

    public VisionInformationRegistrar(CustomRowRestrictionFactory customRowRestriction, TileClassifier tileClassifier,
            BaseSectorScanner baseSectorScanner, VisionPostHandlerRegistrar visionPostHandlerRegistrar)
    {
        this.customRowRestriction = customRowRestriction;
        this.tileClassifier = tileClassifier;
        this.baseSectorScanner = baseSectorScanner;
        this.visionPostHandlerRegistrar = visionPostHandlerRegistrar;
    }

    @Override
    protected void registerInt() {
        BattleState battleState = battleContext.getBattleState();
        UpdatableVisionInformationService service = new VisionInformationServiceImpl(battleState, customRowRestriction,
                tileClassifier, baseSectorScanner);

        battleContext.getBattleDynamicObjectsManager().getAllCharacters().stream()
                .filter(battleModel -> {
                    HasVision hasVision = HasVision.castToHasVision(battleModel);
                    return hasVision != null && hasVision.hasVision();
                })
                .forEach(battleModel -> service.recalculateFieldOfView(battleModel.getId()));

        registerInformationService(VisionInformationService.class, service);
        visionPostHandlerRegistrar.getRegisteredPostHandlers(battleState, service).forEach(this::registerPostHandler);
    }
}
