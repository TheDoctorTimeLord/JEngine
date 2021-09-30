package ru.jengine.utils.fieldofview;

import java.util.function.BiConsumer;

import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.utils.fieldofview.shadowcasting.Quadrant;
import ru.jengine.utils.fieldofview.shadowcasting.RowRestriction;

public interface FieldOfViewCalculator {
    void calculate(Quadrant quadrant, RowRestriction restriction, BiConsumer<Point, String> markAsVisible);
}
