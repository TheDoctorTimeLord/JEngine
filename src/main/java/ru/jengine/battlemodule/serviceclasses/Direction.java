package ru.jengine.battlemodule.serviceclasses;

public enum Direction {
    UP(new Point(0, 1)),
    RIGHT(new Point(1, 0)),
    DOWN(new Point(0, -1)),
    LEFT(new Point(-1, 0));

    private final Point offset;

    Direction(Point offset) {
        this.offset = offset;
    }

    public Point getOffset() {
        return offset;
    }

    public Direction rotateLeft() {
        Direction[] directions = values();
        return directions[(ordinal() - 1 + directions.length) % directions.length];
    }

    public Direction rotateRight() {
        Direction[] directions = values();
        return directions[(ordinal() + 1) % directions.length];
    }
}
