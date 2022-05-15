package ru.jengine.battlemodule.core.battlepresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.utils.CollectionUtils;
import ru.jengine.utils.Logger;

/**
 * Реализует интерфейсы {@link BattleActionRegistrar} (для внутренней работы в бою) и {@link BattleActionPresenter}
 * (для внешнего API боя)
 */
@BattleBeanPrototype
public class BattleActionLogger implements BattleActionRegistrar, BattleActionPresenter {
    private final BattlePresenterActionPublisher publisherDelegate;
    private final List<List<BattleAction>> loggedActions = new ArrayList<>();

    public BattleActionLogger(Logger logger) {
        this.publisherDelegate = new BattlePresenterActionPublisher(logger);
    }

    private final List<BattleAction> loggedActionsForInitialize = new ArrayList<>();
    private volatile boolean wasInitialized = false;

    @Override
    public void registerAction(BattleAction action) {
        if (!wasInitialized) {
            synchronized (loggedActionsForInitialize) {
                if (!wasInitialized) {
                    loggedActionsForInitialize.add(action);
                    return;
                }
            }
        }

        synchronized (loggedActions) {
            if (loggedActions.isEmpty()) {
                loggedActions.add(new ArrayList<>());
            }
            loggedActions.get(loggedActions.size() - 1).add(action);
        }
    }

    @Override
    public void declareEndBattleInitialization() {
        wasInitialized = true;
        Collection<BattleAction> actions;

        synchronized (loggedActionsForInitialize) {
            actions = new ArrayList<>(loggedActionsForInitialize);
            loggedActionsForInitialize.clear();
        }

        publisherDelegate.publish(SubscribeType.INITIALIZATION, actions);
    }

    @Override
    public void declareNewPhase() {
        publishActions(SubscribeType.PHASE, actions -> {
            List<BattleAction> result = CollectionUtils.getLast(actions);
            actions.add(new ArrayList<>());
            return result;
        });
    }

    @Override
    public void declareNewTurn() {
        publishActions(SubscribeType.TURN, actions -> {
            List<BattleAction> actionsOnTurn = CollectionUtils.concat(actions.toArray());
            actions.clear();
            return actionsOnTurn;
        });
    }

    private void publishActions(SubscribeType subscribeType,
            Function<List<List<BattleAction>>, Collection<BattleAction>> synchronizedBattleActionsExtractor)
    {
        Collection<BattleAction> actions;

        synchronized (loggedActions) {
            actions = synchronizedBattleActionsExtractor.apply(loggedActions);
        }

        actions = actions == null ? Collections.emptyList() : actions;
        publisherDelegate.publish(subscribeType, actions);
    }

    @Override
    public void subscribeBattleActions(SubscribeType subscribeType, SubscribeStrategy strategy,
            BattlePresenterActionSubscriber subscriber)
    {
        publisherDelegate.subscribe(subscribeType, strategy, subscriber);
    }

    @Override
    public void unsubscribeBattleActions(SubscribeType subscribeType, BattlePresenterActionSubscriber subscriber) {
        publisherDelegate.unsubscribe(subscribeType, subscriber);
    }
}
