package ru.jengine.utils.fieldofview.shadowcasting;

import java.util.function.BiConsumer;
import java.util.function.Function;

import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.utils.fieldofview.FieldOfViewCalculator;
import ru.jengine.utils.Fraction;

public class ShadowCastingCalculator implements FieldOfViewCalculator {
    private final Function<Point, String> tileClassifier;
    private final SectorScanner startScanner;

    public ShadowCastingCalculator(Function<Point, String> tileClassifier, SectorScanner startScanner) {
        this.tileClassifier = tileClassifier;
        this.startScanner = startScanner;
    }

    @Override
    public void calculate(Quadrant quadrant, RowRestriction restriction, BiConsumer<Point, String> markAsVisible) {
        markAsVisible.accept(quadrant.getStartPosition(), startScanner.getVisibleScope());

        calculateInQuadrant(
                quadrant,
                new Row(1, new Fraction(-1), new Fraction(1)),
                startScanner,
                restriction,
                markAsVisible);
    }

    /**
     * Усовершенствованная версия алгоритма ShadowCasting с возможностью просмотра через некоторые виды стен
     * (например, через полублоки). Доработанная реализация алгоритма взята с сайта
     * https://www.albertford.com/shadowcasting/
     * @param quadrant квадрант, в котором рассчитывается область видимости
     * @param row ряд тайлов, в котором нужно произвести расчёт видимости
     * @param scanner объекта, определяющий условия сканирования
     * @param restriction ограничения для строк (позволяют уточнить диапазон)
     */
    private void calculateInQuadrant(Quadrant quadrant, Row row, SectorScanner scanner, RowRestriction restriction,
            BiConsumer<Point, String> markAsVisible)
    {
        if (!restriction.isAvailableDepth(row.getDepth())) {
            return;
        }

        String previousTile = null;
        String previousTileType = null;
        for (Tile tile : row) {
            Point tilePosition = quadrant.getPositionOnMap(tile);
            if (!restriction.isAvailablePoint(tilePosition)) {
                continue;
            }

            String tileType = tileClassifier.apply(tilePosition);

            if (isBarrier(tileType, scanner) || isSymmetric(row, tile)) {
                markAsVisible.accept(tilePosition, scanner.getVisibleScope());
            }
            if (isFloor(previousTile, scanner) && isBarrier(tileType, scanner)) {
                Row nextRow = row.nextRow();
                nextRow.setEndSlope(slope(tile));
                calculateInQuadrant(quadrant, nextRow, scanner, restriction, markAsVisible);
            }
            if (isDifferentTypes(previousTileType, tileType)) {
                if (previousTileType != null) {
                    SectorScanner newScanner = scanner.getScannerForTileType(previousTileType);
                    if (newScanner != null) {
                        Row newRow = row.nextRow();
                        newRow.setEndSlope(slope(tile));
                        calculateInQuadrant(quadrant, newRow, newScanner, restriction, markAsVisible);
                    }
                }
                row.setStartSlope(row.getStartSlope().max(slope(tile)));
                previousTileType = tileType;
            }

            previousTile = tileType;
        }
        if (isFloor(previousTile, scanner)) {
            calculateInQuadrant(quadrant, row.nextRow(), scanner, restriction, markAsVisible);
        }
        if (isBarrier(previousTile, scanner)) {
            SectorScanner newScanner = scanner.getScannerForTileType(previousTileType);
            if (newScanner != null) {
                calculateInQuadrant(quadrant, row.nextRow(), newScanner, restriction, markAsVisible);
            }
        }
    }

    private static boolean isDifferentTypes(String previousTileType, String tileType) {
        return !tileType.equals(previousTileType);
    }

    private static boolean isFloor(String tileType, SectorScanner scanner) {
        if (tileType == null) {
            return false;
        }
        return !scanner.isBlocking(tileType);
    }

    private static boolean isBarrier(String tileType, SectorScanner scanner) {
        if (tileType == null) {
            return false;
        }
        return scanner.isBlocking(tileType);
    }

    private static boolean isSymmetric(Row row, Tile tile) {
        int depth = row.getDepth();
        Fraction startSlope = row.getStartSlope().multiple(depth);
        Fraction endSlope = row.getEndSlope().multiple(depth);
        return !startSlope.greaterThen(tile.getColumn()) && !endSlope.lessThen(tile.getColumn());
    }

    private static Fraction slope(Tile tile) {
        return new Fraction(2 * tile.getColumn() - 1, 2 * tile.getDepth());
    }
}
