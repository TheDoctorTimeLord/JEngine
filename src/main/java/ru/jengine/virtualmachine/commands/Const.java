package ru.jengine.virtualmachine.commands;

import ru.jengine.virtualmachine.context.CommandContext;

public class Const implements Command {
    private static final byte[] opCode = {0b0000_0000};
    private static final int offset = 2;

    @Override
    public Byte getOpCode() {
        return opCode[0]; // TODO: Byte[] opcode
    }

    @Override
    public int execute(CommandContext context) {
        byte storageIndex = context.readBytes(1)[0];
        Object constValue = context.getElementByIndex(storageIndex);
        context.pushToStack(constValue);
        return offset;
    }

}
