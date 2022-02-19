package ru.jengine.battlemodule.core.commands;

/**
 * Описывает ДОПОЛНИТЕЛЬНЫЕ {@link BattleCommand команды}, которые могут быть зарегистрированы первичными командами
 * @param <P> тип параметров, необходимых для этой команды
 */
public interface AdditionalBattleCommand<P extends CommandExecutionParameters> extends BattleCommand<P> {
}
