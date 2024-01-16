package ru.jengine.virtualmachine.commands;


import ru.jengine.virtualmachine.exceptions.VirtualMachineException;

public class BinaryHandler {
    public static Number sumTwoNumbers(Number firstOp, Number secondOp) {
        if (firstOp instanceof Double || secondOp instanceof Double) {
            return firstOp.doubleValue() + secondOp.doubleValue();
        } else if (firstOp instanceof Float || secondOp instanceof Float) {
            return firstOp.floatValue() + secondOp.floatValue();
        } else if (firstOp instanceof Long || secondOp instanceof Long) {
            return firstOp.longValue() + secondOp.longValue();
        } else {
            return firstOp.intValue() + secondOp.intValue();
        }
    }

    public static Number mulTwoNumbers(Number firstOp, Number secondOp) {
        if (firstOp instanceof Double || secondOp instanceof Double) {
            return firstOp.doubleValue() * secondOp.doubleValue();
        } else if (firstOp instanceof Float || secondOp instanceof Float) {
            return firstOp.floatValue() * secondOp.floatValue();
        } else if (firstOp instanceof Long || secondOp instanceof Long) {
            return firstOp.longValue() * secondOp.longValue();
        } else {
            return firstOp.intValue() * secondOp.intValue();
        }
    }

    public static Number subTwoNumbers(Number firstOp, Number secondOp) {
        if (firstOp instanceof Double || secondOp instanceof Double) {
            return firstOp.doubleValue() - secondOp.doubleValue();
        } else if (firstOp instanceof Float || secondOp instanceof Float) {
            return firstOp.floatValue() - secondOp.floatValue();
        } else if (firstOp instanceof Long || secondOp instanceof Long) {
            return firstOp.longValue() - secondOp.longValue();
        } else {
            return firstOp.intValue() - secondOp.intValue();
        }
    }

    public static Number divTwoNumbers(Number firstOp, Number secondOp) {
        if (secondOp.doubleValue() < 1.0E-10) {
            throw new VirtualMachineException("Division by zero is restricted.");
        }

        if (firstOp instanceof Double || secondOp instanceof Double) {
            return firstOp.doubleValue() / secondOp.doubleValue();
        } else if (firstOp instanceof Float || secondOp instanceof Float) {
            return firstOp.floatValue() / secondOp.floatValue();
        } else if (firstOp instanceof Long || secondOp instanceof Long) {
            return firstOp.doubleValue() / secondOp.longValue();
        } else {
            return firstOp.floatValue() / secondOp.intValue();
        }
    }
}
