package ru.jengine.battlemodule.core.battlepresenter;

import java.util.Collection;

public interface BattlePresenterActionSubscriber {
    void subscribe(SubscribeType subscribeType, Collection<BattleAction> actions);
}
