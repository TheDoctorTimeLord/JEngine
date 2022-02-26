package ru.test.annotation.battle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.serviceclasses.PointPool;
import ru.jengine.utils.fieldofview.FieldOfViewCalculator;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant.NorthQuadrant;
import ru.jengine.utils.fieldofview.shadowcasting.RowRestriction;
import ru.jengine.utils.fieldofview.shadowcasting.SectorScanner;
import ru.jengine.utils.fieldofview.shadowcasting.ShadowCastingCalculator;

import com.google.common.collect.ImmutableSet;

public class TestFOV {
    public static void main(String[] args) {
        char[][] map = {
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '=', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '#', '#', '#', '=', '=', '#', '.', '#', '#', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#', '.', '.', '.', '.', '.'}
        };

        FieldOfViewCalculator fovCalculator = new ShadowCastingCalculator(
                position -> findTileClassByMap(map, position),
                new StartScanner()
        );
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int px = scanner.nextInt();
            int py = scanner.nextInt();

            map[py][px] = '@';

            Map<String, Collection<Point>> visible = new HashMap<>();

            fovCalculator.calculate(
                    new NorthQuadrant(getFromPosition(map)),
                    new DefaultRowRestriction(map[0].length, map.length, 7),
                    (visiblePoint, visibleScope) -> visible.computeIfAbsent(visibleScope, k -> new HashSet<>()).add(visiblePoint));

            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[0].length; x++) {
                    Point point = PointPool.obtain(x, y);
                    if (visible.getOrDefault("all", Collections.emptyList()).contains(point)) {
                        System.out.print(map[y][x]);
                    } else if (visible.getOrDefault("partition", Collections.emptyList()).contains(point)) {
                        char tile = map[y][x];
                        if (tile == '#') {
                            System.out.print(tile);
                        } else {
                            System.out.print('~');
                        }
                    } else {
                        System.out.print(' ');
                    }
                    System.out.print("  ");
                }
                System.out.println();
            }

            map[py][px] = '.';
            visible.clear();
        }
    }

    private static String findTileClassByMap(char[][] map, Point position) {
        char tile = map[position.getY()][position.getX()];

        switch (tile) {
        case '#':
            return "wall";
        case '=':
            return "halfBlock";
        default:
            return "field";
        }
    }

    private static Point getFromPosition(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '@') {
                    return PointPool.obtain(x, y);
                }
            }
        }
        throw new BattleException("@ not found");
    }
}

class DefaultRowRestriction implements RowRestriction {
    private final int width;
    private final int height;
    private final int maxDepth;

    public DefaultRowRestriction(int width, int height, int maxDepth) {
        this.width = width;
        this.height = height;
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean isAvailableDepth(int depth) {
        return depth <= maxDepth;
    }

    @Override
    public boolean isAvailablePoint(Point point) {
        return 0 <= point.getX() && point.getX() < width && 0 <= point.getY() && point.getY() < height;
    }
}

class StartScanner implements SectorScanner {
    private static final Set<String> BLOCKER = ImmutableSet.of("wall", "halfBlock");

    @Override
    public boolean isBlocking(String tileType) {
        return BLOCKER.contains(tileType);
    }

    @Override
    @Nullable
    public SectorScanner getScannerForTileType(String tileType) {
        return tileType.equals("halfBlock") ? new HalfVisionScanner() : null;
    }

    @Override
    public String getVisibleScope() {
        return "all";
    }
}

class HalfVisionScanner extends StartScanner {
    @Override
    @Nullable
    public SectorScanner getScannerForTileType(String tileType) {
        return tileType.equals("halfBlock") ? this : null;
    }

    @Override
    public String getVisibleScope() {
        return "partition";
    }
}

