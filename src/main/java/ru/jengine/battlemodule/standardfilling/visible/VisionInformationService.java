package ru.jengine.battlemodule.standardfilling.visible;

import java.util.Map;
import java.util.Set;

import ru.jengine.battlemodule.core.information.informaionservices.InformationService;
import ru.jengine.battlemodule.core.serviceclasses.Point;

public interface VisionInformationService extends InformationService {
    Map<String, Set<Point>> getVisiblePoints(int visitorId);
}
