package ru.jengine.virtualmachine.commands;

import ru.jengine.virtualmachine.context.CommandContext;
import ru.jengine.virtualmachine.exceptions.VirtualMachineException;

public class Div implements Command {
    private static final byte[] opCode = {0b0000_0100};
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
            try {
                Number result = BinaryHandler.divTwoNumbers((Number) firstOp, (Number) secondOp);
                context.pushToStack(result);
            } catch (VirtualMachineException e) {
                throw e; // TODO: ???
            }
        } else throw new VirtualMachineException("Divide operation uses two instances of Number.");
        return offset;
    }

}
