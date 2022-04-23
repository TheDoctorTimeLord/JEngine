package ru.jengine.battlemodule.standardfilling.visible;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.standardfilling.visible.outside.BaseSectorScanner;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestrictionFactory;
import ru.jengine.battlemodule.standardfilling.visible.outside.TileClassifier;

@BattleBeanPrototype
public class VisionInformationRegistrar extends AbstractContentRegistrar {
    private final CustomRowRestrictionFactory customRowRestriction;
    private final TileClassifier tileClassifier;
    private final BaseSectorScanner baseSectorScanner;

    public VisionInformationRegistrar(CustomRowRestrictionFactory customRowRestriction, TileClassifier tileClassifier,
            BaseSectorScanner baseSectorScanner)
    {
        this.customRowRestriction = customRowRestriction;
        this.tileClassifier = tileClassifier;
        this.baseSectorScanner = baseSectorScanner;
    }

    @Override
    protected void registerInt() {
        UpdatableVisionInformationService service = new VisionInformationServiceImpl(battleContext.getBattleState(),
                customRowRestriction, tileClassifier, baseSectorScanner);

        battleContext.getBattleDynamicObjectsManager().getAllCharacters().stream()
                .filter(battleModel -> HasVision.castToHasVision(battleModel) != null)
                .forEach(battleModel -> service.recalculateFieldOfView(battleModel.getId()));

        registerInformationService(VisionInformationService.class, service);
        registerPostHandler(new RecalculateAfterMoveHandler(service));
    }
}
