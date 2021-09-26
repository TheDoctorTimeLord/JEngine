package ru.jengine.battlemodule.standardfilling.fieldofview.shadowcasting;

import javax.annotation.Nullable;

public interface SectorScanner {
    boolean isBlocking(String tileType);
    @Nullable SectorScanner getScannerForTileType(String tileType);
    String getVisibleScope();
}
