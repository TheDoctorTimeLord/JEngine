package ru.jengine.battlemodule.standardfilling.fieldofview.shadowcasting;

import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import ru.jengine.utils.Fraction;

public class Row implements Iterable<Tile> {
    private final int depth;
    private Fraction startSlope;
    private Fraction endSlope;

    public Row(int depth, Fraction startSlope, Fraction endSlope) {
        this.depth = depth;
        this.startSlope = startSlope;
        this.endSlope = endSlope;
    }

    public Fraction getStartSlope() {
        return startSlope;
    }

    public Fraction getEndSlope() {
        return endSlope;
    }

    public void setStartSlope(Fraction slope) {
        this.startSlope = slope;
    }

    public void setEndSlope(Fraction slope) {
        this.endSlope = slope;
    }

    public int getDepth() {
        return depth;
    }

    public Row nextRow() {
        return new Row(depth + 1, startSlope, endSlope);
    }

    @NotNull
    @Override
    public Iterator<Tile> iterator() {
        return new RowIterator(roundTiesUp(startSlope.multiple(depth)), roundTiesDown(endSlope.multiple(depth)), depth);
    }

    private static int roundTiesUp(Fraction fraction) {
        return (int)Math.floor(((double)fraction.getNumerator() / fraction.getDenominator()) + 0.5);
    }

    private static int roundTiesDown(Fraction fraction) {
        return (int)Math.ceil(((double)fraction.getNumerator() / fraction.getDenominator()) - 0.5);
    }

    private static class RowIterator implements  Iterator<Tile> {
        private final int toColumn;
        private final int depth;
        private int currentColumn;

        private RowIterator(int fromColumn, int toColumn, int depth) {
            this.currentColumn = fromColumn;
            this.toColumn = toColumn;
            this.depth = depth;
        }

        @Override
        public boolean hasNext() {
            return currentColumn <= toColumn;
        }

        @Override
        public Tile next() {
            return new Tile(depth, currentColumn++);
        }
    }
}
