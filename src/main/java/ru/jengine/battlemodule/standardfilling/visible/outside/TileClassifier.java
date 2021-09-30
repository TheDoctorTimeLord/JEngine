package ru.jengine.battlemodule.standardfilling.visible.outside;

import java.util.List;

import ru.jengine.battlemodule.core.models.BattleModel;

public interface TileClassifier {
    String classify(List<BattleModel> modelsOnTile);
}
