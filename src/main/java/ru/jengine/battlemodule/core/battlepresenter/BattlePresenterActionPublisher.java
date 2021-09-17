package ru.jengine.battlemodule.core.battlepresenter;

import static ru.jengine.utils.CollectionUtils.concat;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BattlePresenterActionPublisher {
    private final Map<SubscribeType, List<SubscriberContext>> subscribersByType =
            new ConcurrentHashMap<>();

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

    public void unsubscribe(SubscribeType subscribeType, BattlePresenterActionSubscriber subscriber) {
        synchronized (subscribersByType) {
            subscribersByType.get(subscribeType)
                    .removeIf(subscriberContext -> subscriberContext.getSubscriber().equals(subscriber));
        }
    }

    public void publish(SubscribeType subscribeType, Collection<BattleAction> loggedActions) {
        subscribersByType.computeIfPresent(subscribeType, (type, subscribers) -> {
            for (SubscriberContext subscriber : subscribers) {
                subscriber
                        .getSubscriber()
                        .subscribe(subscribeType, subscriber.getStrategy().filterAvailableAction(loggedActions));
            }
            return subscribers;
        });
    }

    private static class SubscriberContext {
        private final SubscribeStrategy strategy;
        private final BattlePresenterActionSubscriber subscriber;

        private SubscriberContext(SubscribeStrategy strategy,
                BattlePresenterActionSubscriber subscriber) {
            this.strategy = strategy;
            this.subscriber = subscriber;
        }

        public SubscribeStrategy getStrategy() {
            return strategy;
        }

        public BattlePresenterActionSubscriber getSubscriber() {
            return subscriber;
        }
    }
}
