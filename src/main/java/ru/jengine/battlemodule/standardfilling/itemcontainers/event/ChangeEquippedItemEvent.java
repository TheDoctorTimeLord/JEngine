package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.models.BattleModel;

/**
 * Событие, инициирующее смену предмета в одном контейнере
 */
public class ChangeEquippedItemEvent extends BattleEvent {
    private final BattleModel model;
    private final String fromContainer;
    private final int indexFromContainer;
    private final String toContainer;
    private final int indexToContainer;

    /**
     * @param model объект, у которого изменяется предмет
     * @param fromContainer код контейнера, в котором лежит текущий меняемый предмет
     * @param indexFromContainer индекс предмета в fromContainer
     * @param toContainer код контейнера, в котором лежит новый предмет
     * @param indexToContainer индекс предмета в toContainer
     */
    public ChangeEquippedItemEvent(BattleModel model, String fromContainer, int indexFromContainer, String toContainer,
            int indexToContainer) {
        this.model = model;
        this.fromContainer = fromContainer;
        this.indexFromContainer = indexFromContainer;
        this.toContainer = toContainer;
        this.indexToContainer = indexToContainer;
    }

    public AttributesContainer getAttributeContainer() {
        return model.getAttributes();
    }

    public String getFromContainer() {
        return fromContainer;
    }

    public int getIndexFromContainer() {
        return indexFromContainer;
    }

    public int getIndexToContainer() {
        return indexToContainer;
    }

    public int getModelId() {
        return model.getId();
    }

    public String getToContainer() {
        return toContainer;
    }
}
