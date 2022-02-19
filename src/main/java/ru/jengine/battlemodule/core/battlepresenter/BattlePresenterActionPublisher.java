package ru.jengine.battlemodule.core.battlepresenter;

import static ru.jengine.utils.CollectionUtils.concat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс, реализующий компонент "Наблюдаемый" из шаблона проектирования "Наблюдаемый-Наблюдатель" для API получения
 * информации о бое
 */
public class BattlePresenterActionPublisher {
    private final Map<SubscribeType, List<SubscriberContext>> subscribersByType =
            new ConcurrentHashMap<>();

    /**
     * Подписывает внешнего получателя на события в бою
     * @param subscribeType тип информации, который получатель хочет получить
     * @param strategy стратегия фильтрации получаемой информации (может быть полезно, если вы хотите получать
     *                 информацию, которая полезна только конкретному персонажу)
     * @param subscriber объект получателя, которому будут посылаться уведомления о происходящем в бою
     */
    public void subscribe(SubscribeType subscribeType, SubscribeStrategy strategy,
            BattlePresenterActionSubscriber subscriber)
    {
        subscribersByType.merge(
                subscribeType,
                concat(new SubscriberContext(strategy, subscriber)),
                (oldValue, newValue) -> {
                    oldValue.addAll(newValue);
                    return oldValue;
                });
    }

    /**
     * Отписывает внешнего получателя от событий в бою
     * @param subscribeType тип информации, который получатель получал
     * @param subscriber объект получателя, которому будут посылаться уведомления о происходящем в бою
     */
    public void unsubscribe(SubscribeType subscribeType, BattlePresenterActionSubscriber subscriber) {
        synchronized (subscribersByType) {
            subscribersByType.get(subscribeType)
                    .removeIf(subscriberContext -> subscriberContext.subscriber.equals(subscriber));
        }
    }

    /**
     * Публикует информацию, на которую подписаны получатели информации в бою. Те, кому будет отправлена информация,
     * зависят от типа подписки
     * @param subscribeType тип информации, который получает получатель
     * @param loggedActions вся информация для отправки
     */
    public void publish(SubscribeType subscribeType, Collection<BattleAction> loggedActions) {
        subscribersByType.computeIfPresent(subscribeType, (type, subscribers) -> {
            for (SubscriberContext subscriber : subscribers) {
                subscriber
                        .subscriber
                        .subscribe(subscribeType, new ArrayList<>(subscriber.strategy.filterAvailableAction(loggedActions)));
            }
            return subscribers;
        });
    }

    /**
     * Внутренний дата-класс для хранения информации о подписчиках на действия в бою
     */
    private static class SubscriberContext {
        private final SubscribeStrategy strategy;
        private final BattlePresenterActionSubscriber subscriber;

        private SubscriberContext(SubscribeStrategy strategy,
                BattlePresenterActionSubscriber subscriber) {
            this.strategy = strategy;
            this.subscriber = subscriber;
        }
    }
}
