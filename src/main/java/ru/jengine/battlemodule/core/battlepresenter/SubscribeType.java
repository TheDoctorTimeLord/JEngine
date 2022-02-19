package ru.jengine.battlemodule.core.battlepresenter;

/**
 * Тип подписки на события в бою
 */
public enum SubscribeType {
    /** Информация о начальном состоянии боя */
    INITIALIZATION,
    /** Информация, которая была получена в течении одной фазы */
    PHASE,
    /** Информация, которая была получена в течении одного хода */
    TURN
}
