package ru.test.annotation.battle.information.vision;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.dynamicmodel.DynamicModel;
import ru.jengine.battlemodule.standardfilling.visible.outside.BaseSectorScanner;
import ru.jengine.battlemodule.standardfilling.visible.outside.TileClassifier;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.utils.fieldofview.shadowcasting.SectorScanner;

@Bean
public class SimpleSectorScanner implements BaseSectorScanner, TileClassifier {
    private static final String VISIBLE = "visible";
    private static final String SOLID = "solid";

    @Override
    public String classify(List<BattleModel> modelsOnTile) {
        return modelsOnTile.stream().allMatch(model -> model instanceof DynamicModel)
                ? VISIBLE
                : SOLID;
    }

    @Override
    public boolean isBlocking(String tileType) {
        return SOLID.equals(tileType);
    }

    @Nullable
    @Override
    public SectorScanner getScannerForTileType(String tileType) {
        return SOLID.equals(tileType)
                ? null
                : this;
    }

    @Override
    public String getVisibleScope() {
        return VISIBLE;
    }
}
