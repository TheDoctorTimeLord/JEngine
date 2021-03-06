package ru.jengine.battlemodule.core.serviceclasses;

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

    public static Direction getByOffset(Point offset) {
        for (Direction direction : values()) {
            if (direction.getOffset().equals(offset)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Offset [" + offset + "] doesn't match any Direction");
    }
}
