package ru.jengine.battlemodule.standardfilling.visible;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestrictionFactory;
import ru.jengine.utils.fieldofview.FieldOfViewCalculator;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant.EastQuadrant;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant.NorthQuadrant;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant.SouthQuadrant;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant.WestQuadrant;
import ru.jengine.utils.fieldofview.shadowcasting.ShadowCastingCalculator;
import ru.jengine.battlemodule.standardfilling.visible.outside.BaseSectorScanner;
import ru.jengine.battlemodule.standardfilling.visible.outside.CustomRowRestriction;
import ru.jengine.battlemodule.standardfilling.visible.outside.TileClassifier;

public class VisionInformationServiceImpl implements UpdatableVisionInformationService {
    private final Map<Integer, Map<String, Set<Point>>> fovs = new ConcurrentHashMap<>();
    private final FieldOfViewCalculator calculator;
    private final BattleState battleState;
    private final CustomRowRestrictionFactory restrictionFactory;

    public VisionInformationServiceImpl(BattleState battleState, CustomRowRestrictionFactory restrictionFactory,
            TileClassifier classifier, BaseSectorScanner scanner)
    {
        this.battleState = battleState;
        this.restrictionFactory = restrictionFactory;

        calculator = new ShadowCastingCalculator(
                point -> classifier.classify(battleState.getModelsOnPosition(point)),
                scanner
        );
    }

    @Override
    public Map<String, Set<Point>> getVisiblePoints(int visitorId) {
        return fovs.getOrDefault(visitorId, Collections.emptyMap());
    }

    @Override
    public Set<Point> getVisiblePoints(int visitorId, String visibleScope) {
        return fovs.getOrDefault(visitorId, Collections.emptyMap()).getOrDefault(visibleScope, Collections.emptySet());
    }

    @Override
    public void recalculateFieldOfView(int visitorId) {
        HasVision hasVision = HasVision.castToHasVision(battleState.resolveId(visitorId));
        if (hasVision == null || !hasVision.hasVision()) {
            return;
        }

        Map<String, Set<Point>> visible = new HashMap<>();

        CustomRowRestriction rowRestriction = restrictionFactory.createRowRestriction(hasVision, battleState);

        Quadrant quadrant = calculateQuadrant(hasVision.getDirection(), hasVision.getPosition());
        calculator.calculate(quadrant, rowRestriction, (position, visibleType) ->
                visible.computeIfAbsent(visibleType, k -> new HashSet<>()).add(position));
        fovs.put(visitorId, visible);
    }

    private static Quadrant calculateQuadrant(Direction direction, Point position) {
        return switch (direction) {
            case UP -> new NorthQuadrant(position);
            case DOWN -> new SouthQuadrant(position);
            case LEFT -> new WestQuadrant(position);
            case RIGHT -> new EastQuadrant(position);
            default -> throw new BattleException(
                    "Direction can not be not UP, DOWN, LEFT or RIGHT. Direction=" + direction);
        };
    }
}
