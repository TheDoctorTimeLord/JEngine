package ru.jengine.utils.fieldofview.shadowcasting;

import ru.jengine.battlemodule.core.exceptions.BattleException;

public class Tile {
    private final int depth;
    private final int column;

    public Tile(int depth, int column) {
        if (depth <= 0) {
            throw new BattleException("Depth must be greater then 0");
        }

        this.depth = depth;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getDepth() {
        return depth;
    }
}
