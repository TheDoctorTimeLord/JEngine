package ru.jengine.battlemodule.core.commands.executioncontexts;

import ru.jengine.battlemodule.core.commands.CommandExecutionParameters;

/**
 * Специальный класс, декларирующий, что команда не имеет дополнительных параметров
 */
public class NoneParameters implements CommandExecutionParameters {
    public static final NoneParameters INSTANCE = new NoneParameters();

    private NoneParameters() {}
}
