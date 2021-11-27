package ru.jengine.battlemodule.standardfilling.visible;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.standardfilling.visible.outside.BaseSectorScanner;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestriction;
import ru.jengine.battlemodule.standardfilling.visible.outside.TileClassifier;
import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.utils.fieldofview.shadowcasting.SectorScanner;

@BattleBeanPrototype
public class VisionInformationRegistrar extends AbstractContentRegistrar {
    private final CustomRowRestriction customRowRestriction;
    private final TileClassifier tileClassifier;
    private final BaseSectorScanner baseSectorScanner;

    @Inject //ЗАГЛУШКА
    public VisionInformationRegistrar() {
        this(
                new CustomRowRestriction() {
                    @Override
                    public boolean isAvailableDepth(int depth) {
                        return depth <= 4;
                    }

                    @Override
                    public boolean isAvailablePoint(Point point) {
                        return true;
                    }
                },
                new TileClassifier() {
                    @Override
                    public String classify(List<BattleModel> modelsOnTile) {
                        return "default";
                    }
                },
                new BaseSectorScanner() {
                    @Override
                    public boolean isBlocking(String tileType) {
                        return false;
                    }

                    @Nullable
                    @Override
                    public SectorScanner getScannerForTileType(String tileType) {
                        return null;
                    }

                    @Override
                    public String getVisibleScope() {
                        return "always";
                    }
                }
        );
    }

    public VisionInformationRegistrar(CustomRowRestriction customRowRestriction, TileClassifier tileClassifier,
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
