package ru.jengine.battlemodule.standardfilling.visible;

public interface UpdatableVisionInformationService extends VisionInformationService {
    void recalculateFieldOfView(int visitorId);
}
