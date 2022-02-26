package ru.jengine.battlemodule.core.serviceclasses;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PointPool {
    private static volatile boolean isEnable = true;
    private static final Map<Integer, Map<Integer, Point>> pointPool = new ConcurrentHashMap<>();

    public static Point obtain(Integer x, Integer y) { //TODO а какой оптимайз по памяти по сравнению с new Point()
        if (isEnable) {
            Map<Integer, Point> yCoordinatePool = pointPool.computeIfAbsent(x, k -> new ConcurrentHashMap<>());
            return yCoordinatePool.computeIfAbsent(y, yCoordinate -> new Point(x, yCoordinate));
        }
        return new Point(x, y);
    }

    public static void clearPool() {
        pointPool.clear();
    }

    public static void setPoolState(boolean isEnable) {
        if (PointPool.isEnable && !isEnable) {
            clearPool();
        }
        PointPool.isEnable = isEnable;
    }
}
