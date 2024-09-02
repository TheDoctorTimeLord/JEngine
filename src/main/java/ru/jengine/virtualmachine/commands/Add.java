package ru.jengine.virtualmachine.commands;

import ru.jengine.virtualmachine.context.CommandContext;
import ru.jengine.virtualmachine.exceptions.VirtualMachineException;

public class Add implements Command {
    private static final byte[] opCode = {0b0000_0001};
    private static final int offset = 1;

    @Override
    public Byte getOpCode() {
        return opCode[0];
    }

    @Override
    public int execute(CommandContext context) {
        Object firstOp = context.popFromStack();
        Object secondOp = context.popFromStack();

        if (firstOp instanceof Number && secondOp instanceof Number) {
            Number result = BinaryHandler.sumTwoNumbers((Number) firstOp, (Number) secondOp);
            context.pushToStack(result);
        } else throw new VirtualMachineException("Add operation uses two instances of Number.");

        return offset;
    }
}
