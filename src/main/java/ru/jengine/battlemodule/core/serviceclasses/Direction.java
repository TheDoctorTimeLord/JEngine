package ru.jengine.battlemodule.core.serviceclasses;

/**
 * Направления взгляда персонажей.
 */
public enum Direction {
    UP(PointPool.obtain(0, 1)),
    RIGHT(PointPool.obtain(1, 0)),
    DOWN(PointPool.obtain(0, -1)),
    LEFT(PointPool.obtain(-1, 0));

    private final Point offset;

    Direction(Point offset) {
        this.offset = offset;
    }

    /**
     * Смещение для текущего направления взгляда
     */
    public Point getOffset() {
        return offset;
    }

    /**
     * Возвращает направление, которое повёрнуто влево относительно текущего направления
     */
    public Direction rotateLeft() {
        Direction[] directions = values();
        return directions[(ordinal() - 1 + directions.length) % directions.length];
    }

    /**
     * Возвращает направление, которое повёрнуто вправо относительно текущего направления
     */
    public Direction rotateRight() {
        Direction[] directions = values();
        return directions[(ordinal() + 1) % directions.length];
    }

    /**
     * Возвращает направление, соответствующее смещению
     * @param offset смещение
     * @throws IllegalArgumentException если такого направления нет
     */
    public static Direction getByOffset(Point offset) {
        for (Direction direction : values()) {
            if (direction.getOffset().equals(offset)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Offset [" + offset + "] doesn't match any Direction");
    }
}
