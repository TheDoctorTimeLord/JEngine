package ru.jengine.battlemodule.core.scheduler;

import ru.jengine.taskscheduler.Task;

/**
 * Отвечает за регистрацию задач в некоторой категории планировщика задач в бою
 */
public interface SchedulerTaskRegistrar {
    /**
     * Добавляет задачу, которая будет после завершения инициализации боя
     * @param task запланированная задача
     */
    void addTaskAfterInitializeBattle(Task task);

    /**
     * Добавляет задачу, которая будет исполнена в момент перед началом нового хода
     * @param task запланированная задача
     */
    void addTaskBeforeTurn(Task task);

    /**
     * Добавляет задачу, которая будет исполнена в момент после окончания фазы
     * @param task запланированная задача
     */
    void addTaskAfterPhase(Task task);

    /**
     * Добавляет задачу, которая будет исполнена в момент после окончания хода
     * @param task запланированная задача
     */
    void addTaskAfterTurn(Task task);
}
