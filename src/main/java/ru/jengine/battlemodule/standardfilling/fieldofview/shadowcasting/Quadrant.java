package ru.jengine.battlemodule.standardfilling.fieldofview.shadowcasting;

import ru.jengine.battlemodule.core.serviceclasses.Point;

public abstract class Quadrant {
    protected final Point startFieldOfView;

    private Quadrant(Point startFieldOfView) {
        this.startFieldOfView = startFieldOfView;
    }

    public abstract Point getPositionOnMap(Tile tile);

    public Point getStartPosition() {
        return startFieldOfView;
    }

    public static class NorthQuadrant extends Quadrant {
        public NorthQuadrant(Point startFieldOfView) {
            super(startFieldOfView);
        }

        @Override
        public Point getPositionOnMap(Tile tile) {
            return new Point(
                    startFieldOfView.getX() + tile.getColumn(),
                    startFieldOfView.getY() + tile.getDepth());
        }
    }

    public static class EastQuadrant extends Quadrant {
        public EastQuadrant(Point startFieldOfView) {
            super(startFieldOfView);
        }

        @Override
        public Point getPositionOnMap(Tile tile) {
            return new Point(
                    startFieldOfView.getX() + tile.getDepth(),
                    startFieldOfView.getY() - tile.getColumn()
            );
        }
    }

    public static class SoulsQuadrant extends Quadrant {
        public SoulsQuadrant(Point startFieldOfView) {
            super(startFieldOfView);
        }

        @Override
        public Point getPositionOnMap(Tile tile) {
            return new Point(
                    startFieldOfView.getX() - tile.getColumn(),
                    startFieldOfView.getY() - tile.getDepth()
            );
        }
    }

    public static class WestQuadrant extends Quadrant {
        public WestQuadrant(Point startFieldOfView) {
            super(startFieldOfView);
        }

        @Override
        public Point getPositionOnMap(Tile tile) {
            return new Point(
                    startFieldOfView.getX() - tile.getDepth(),
                    startFieldOfView.getY() + tile.getColumn()
            );
        }
    }
}
