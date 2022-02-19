package ru.jengine.battlemodule.core.commands;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.models.BattleModel;

/**
 * Описывает фабрику (см. шаблон проектирования "Фабрика"), создающую объект команды. Каждая фабрика производит
 * команды ТОЛЬКО одного типа. В обязанности фабрики также входит определить может ли конкретный динамический объект
 * выполнять данную команду в бою и доступна ли эта команда объекту в текущем ходу.
 * @param <P> тип параметров, необходимых для производимых команд
 * @param <C> тип команд, которые будет производить данная фабрика
 */
public interface BattleCommandFactory<P extends CommandExecutionParameters, C extends BattleCommand<P>> {
    boolean canExecute(BattleModel model, BattleContext battleContext);
    boolean isAvailableCommand(BattleModel model, BattleContext battleContext);
    C createBattleCommand(BattleModel model, BattleContext battleContext);
}
