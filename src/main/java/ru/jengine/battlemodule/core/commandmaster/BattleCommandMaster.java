package ru.jengine.battlemodule.core.commandmaster;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.scheduler.SchedulerTaskExecutor;

/**
 * Выполняет действия всех динамических объектов в рамках одного хода. В его ответственность входит получение
 * доступных действий всех персонажей, опрос дополнительных параметров команд, если это необходимо, а также их
 * последовательная обработка и применение к текущему состоянию боя
 */
public interface BattleCommandMaster extends CommandsOnPhaseRegistrar {
    /**
     * Симулирует один ход боя, получая действия всех персонажей и выполняя их
     * @param battleContext контекст текущего боя
     * @param taskExecutor объект, отвечающий за наступление событий выполнения задач в планировщике задач
     */
    void takeTurn(BattleContext battleContext, SchedulerTaskExecutor taskExecutor);
}
