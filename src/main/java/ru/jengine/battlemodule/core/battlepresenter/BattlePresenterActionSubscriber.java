package ru.jengine.battlemodule.core.battlepresenter;

import java.util.Collection;

/**
 * Класс, реализующий компонент "Наблюдатель" из шаблона проектирования "Наблюдаемый-Наблюдатель" для API получения
 * информации о бое
 */
public interface BattlePresenterActionSubscriber {
    /**
     * Метод, который будет вызван при получении действий в бою получателем (callback)
     * @param subscribeType тип информации, в рамках которого получаются действия
     * @param actions все произошедшие действия (период, за который собраны действия, зависит от типа информации)
     */
    void subscribe(SubscribeType subscribeType, Collection<BattleAction> actions);
}
