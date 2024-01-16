package ru.jengine.virtualmachine.context;

public interface CommandContext {

    Object popFromStack();

    Object peekFromStack();

    void pushToStack(Object element);

    Object getElementByIndex(Byte index);

    byte[] readBytes(int n);
}
