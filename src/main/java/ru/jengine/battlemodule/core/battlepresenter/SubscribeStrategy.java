package ru.jengine.battlemodule.core.battlepresenter;

import java.util.Collection;
import java.util.Collections;

/**
 * Описывает стратегию фильтрации информации для подписчика на текущий бой
 */
public interface SubscribeStrategy {
    /**
     * Фильтрует доступные для пересылки события
     * @param loggedActions все доступные для пересылки события
     * @return доступные для пересылки события с учётом фильтрации
     */
    Collection<BattleAction> filterAvailableAction(Collection<BattleAction> loggedActions);

    /**
     * Стратегия, пропускающая все доступные события (фильтрация отсутствует)
     */
    SubscribeStrategy ALL_ACTIONS_SUBSCRIBE_STRATEGY = actions -> actions;
    /**
     * Стратегия, не пропускающая ни одного доступного события (фильтрует абсолютно все события). Может быть
     * полезна, если необходимо получить только факт того, что один из {@link SubscribeType} произошёл
     */
    SubscribeStrategy INFORMATION_ABOUT_SUBSCRIBE = actions -> Collections.emptyList();
}
