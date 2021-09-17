package ru.jengine.battlemodule.core.battlepresenter;

import java.util.Collection;
import java.util.Collections;

public interface SubscribeStrategy {
    Collection<BattleAction> filterAvailableAction(Collection<BattleAction> loggedActions);

    SubscribeStrategy ALL_ACTIONS_SUBSCRIBE_STRATEGY = actions -> actions;
    SubscribeStrategy INFORMATION_ABOUT_SUBSCRIBE = actions -> Collections.emptyList();
}
