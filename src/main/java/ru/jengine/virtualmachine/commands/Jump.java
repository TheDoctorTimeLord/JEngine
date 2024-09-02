package ru.jengine.virtualmachine.commands;

import ru.jengine.virtualmachine.context.CommandContext;

public class Jump implements Command {

    private static final byte[] opCode = {0b0111_1111};
    private static final int offset = 1;

    @Override
    public Byte getOpCode() {
        return opCode[0];
    }

    @Override
    public int execute(CommandContext context) {
        byte storageIndex = context.readBytes(1)[0];
        Integer offsetValue = (Integer) context.getElementByIndex(storageIndex);
        return offsetValue;
    }
}
