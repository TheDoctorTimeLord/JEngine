package ru.jengine.battlemodule.core.battlepresenter;

public interface BattleActionPresenter {
    void subscribeBattleActions(SubscribeType subscribeType, SubscribeStrategy strategy,
            BattlePresenterActionSubscriber subscriber);

    void unsubscribeBattleActions(SubscribeType subscribeType, BattlePresenterActionSubscriber subscriber);
}
