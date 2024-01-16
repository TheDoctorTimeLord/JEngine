package ru.jengine.virtualmachine.context;

import ru.jengine.virtualmachine.VMStack;

import java.util.List;

public class ScriptContext implements CommandContext {
    private final VMStack stack = new VMStack(1024);
    private final List<Object> storage;
    private final byte[] code;
    private int instructionPointer = 0;

    public ScriptContext(List<Object> storage, byte[] code) {
        this.storage = storage;
        this.code = code;
    }

    public int getInstructionPointer() {
        return instructionPointer;
    }

    public Object popFromStack() { // TODO: check empty
        return stack.pop();
    }

    public Object peekFromStack() {
        return stack.peek();
    }

    public void pushToStack(Object element) {
        try {
            stack.push(element);
        } catch (StackOverflowError e) {
            throw e; // TODO: ???
        }
    }

    public Object getElementByIndex(Byte index) {
        return storage.get(index.intValue());
    }

    public void moveInstructionPointer(int offset) {
        this.instructionPointer += offset;
    }

    public byte[] readBytes(int n) {
        byte[] slice = new byte[n];
        System.arraycopy(code, instructionPointer + 1, slice, 0, n);
        return slice;
    }

    public byte[] getCode() {
        return code;
    }

    public boolean isScriptRunning() {
        return instructionPointer < code.length;
    }
}
