package ru.jengine.virtualmachine.commands;

import ru.jengine.virtualmachine.context.CommandContext;

public interface Command {
    Byte getOpCode();

    int execute(CommandContext context);

}
