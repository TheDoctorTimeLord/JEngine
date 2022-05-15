package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules;

import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;

public class ChangedAttributesContext {
   /** модель объекта в бою, которому принадлежит изменённый атрибут */
    private final BattleModel model;
    /** атрибут, который был изменён и подошёл под условия из {@link AttributeRule#getHandledAttributeCodes()} */
    private final BattleAttribute changedAttribute;
    /** Диспетчер событий в бою */
    private final DispatcherBattleWrapper dispatcher;

    public ChangedAttributesContext(BattleModel model, BattleAttribute changedAttribute,
            DispatcherBattleWrapper dispatcher) {
        this.model = model;
        this.changedAttribute = changedAttribute;
        this.dispatcher = dispatcher;
    }

    public BattleAttribute getChangedAttribute() {
        return changedAttribute;
    }

    public DispatcherBattleWrapper getDispatcher() {
        return dispatcher;
    }

    public BattleModel getModel() {
        return model;
    }
}
