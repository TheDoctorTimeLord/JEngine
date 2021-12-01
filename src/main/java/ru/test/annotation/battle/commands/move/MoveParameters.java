package ru.test.annotation.battle.commands.move;

import java.util.Collections;
import java.util.Set;

import ru.jengine.battlemodule.core.commands.CommandExecutionParameters;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public class MoveParameters implements CommandExecutionParameters {
    private final Set<Point> availablePoints;
    private Point answer;

    public MoveParameters(Set<Point> availablePoints) {
        this.availablePoints = availablePoints != null ? availablePoints : Collections.emptySet();
    }

    public Set<Point> getAvailablePoints() {
        return availablePoints;
    }

    public Point getAnswer() {
        return answer;
    }

    public void publishAnswer(Point answer) {
        this.answer = answer;
    }
}
