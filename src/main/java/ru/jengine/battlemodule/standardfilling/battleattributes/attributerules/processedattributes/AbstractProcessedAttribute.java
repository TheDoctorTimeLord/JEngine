package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes;

import java.util.List;

import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRule;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRulesManager;

/**
 * Общий класс для атрибутов, находящихся в процессе обработки {@link AttributeRulesManager}'ом. Данная абстракция
 * позволяет добиться обработки конкретных действий над атрибутами, избегая их сегрегирования по типу обработки
 * (удаление, изменение и т.д.)
 */
public abstract class AbstractProcessedAttribute {
    protected final BattleAttribute attribute;

    public AbstractProcessedAttribute(BattleAttribute attribute) {
        this.attribute = attribute;
    }

    public BattleAttribute getAttribute() {
        return attribute;
    }

    /**
     * Запускает обработку атрибута, в соответствии с типом его обработки (удаление, изменение и т.д.)
     * @param attributeRule конкретный обработчик изменённых атрибутов
     * @param model модель объекта в бою, у которого происходит изменение атрибута
     * @param dispatcher диспетчер событий в бою
     * @return список изменённых атрибутов, в результате обработки
     */
    public abstract List<AbstractProcessedAttribute> processAttribute(AttributeRule attributeRule, BattleModel model,
            DispatcherBattleWrapper dispatcher);
}
