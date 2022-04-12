package ru.jengine.battlemodule.core.scheduler;

/**
 * Отвечает за выполнения некоторой категории задач планировщика в бою
 */
public interface SchedulerTaskExecutor {
    /**
     * Выполняет все запланированные на момент завершения инициализации боя задачи
     */
    void executeAfterInitializeBattle();

    /**
     * Выполняет все запланированные на момент начала хода задачи
     */
    void executeBeforeTurn();

    /**
     * Выполняет все запланированные на момент окончания фазы задачи
     */
    void executeAfterPhase();

    /**
     * Выполняет все запланированные на момент окончания хода задачи
     */
    void executeAfterTurn();
}
