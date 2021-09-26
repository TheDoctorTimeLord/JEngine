package ru.jengine.battlemodule.standardfilling.fieldofview.shadowcasting;

import ru.jengine.battlemodule.core.serviceclasses.Point;

public interface RowRestriction { //TODO поменять на просмотр ограничения по строкам, а не по отдельной информации
    boolean isAvailableDepth(int depth);
    boolean isAvailablePoint(Point point);
}
