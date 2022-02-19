package ru.jengine.battlemodule.core.battlepresenter;

/**
 * Описывает объект, через который можно осуществлять подписку (и отписку) на события, происходящие в бою. С
 * технической точки зрения каждый бой предоставляет какую-либо информацию о ходе боя только поведениям, управляют
 * динамическими персонажами в бою. Для того чтобы была возможность получить информацию о бое для других объектов,
 * используется данный интерфейс. Интерфейс подразумевает работу в режиме шаблона проектирования
 * "Наблюдаемый-Наблюдатель"
 */
public interface BattleActionPresenter {
    /**
     * Подписывает внешнего получателя на события в бою
     * @param subscribeType тип информации, который получатель хочет получить
     * @param strategy стратегия фильтрации получаемой информации (может быть полезно, если вы хотите получать
     *                 информацию, которая полезна только конкретному персонажу)
     * @param subscriber объект получателя, которому будут посылаться уведомления о происходящем в бою
     */
    void subscribeBattleActions(SubscribeType subscribeType, SubscribeStrategy strategy,
            BattlePresenterActionSubscriber subscriber);

    /**
     * Отписывает внешнего получателя от событий в бою
     * @param subscribeType тип информации, который получатель получал
     * @param subscriber объект получателя, которому будут посылаться уведомления о происходящем в бою
     */
    void unsubscribeBattleActions(SubscribeType subscribeType, BattlePresenterActionSubscriber subscriber);
}
