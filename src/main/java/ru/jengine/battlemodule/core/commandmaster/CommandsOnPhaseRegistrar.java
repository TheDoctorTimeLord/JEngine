package ru.jengine.battlemodule.core.commandmaster;

import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;

/**
 * Описывает объект, отвечающий за регистрирование команд на следующую фазу боя.
 */
public interface CommandsOnPhaseRegistrar {
    /**
     * Регистрирует команду на следующую фазу боя.
     * @param battleModelId ID динамического объекта над которым будет выполняться команда
     * @param battleCommand команда, которая должна быть исполнена в следующей фазе
     */
    void registerCommandOnNextPhase(int battleModelId, AdditionalBattleCommand<?> battleCommand);
}
