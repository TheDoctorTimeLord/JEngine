package ru.jengine.battlemodule.standardfilling.fieldofview;

import ru.jengine.battlemodule.standardfilling.fieldofview.shadowcasting.Quadrant;
import ru.jengine.battlemodule.standardfilling.fieldofview.shadowcasting.RowRestriction;

public interface FieldOfViewCalculator {
    void calculate(Quadrant quadrant, RowRestriction restriction);
}
