package ru.jengine.virtualmachine;

import java.util.ArrayDeque;

public class VMStack {
    private final int stackSize;
    private final ArrayDeque<Object> stack;

    public VMStack(int capacity) {
        stackSize = capacity;
        stack = new ArrayDeque<>(capacity);
    }

    public Object pop() {
        return stack.pop();
    }

    public void push(Object element) {
        if (stack.size() == stackSize) {
            throw new StackOverflowError("VM's stack has overflowed.");
        }
        stack.push(element);
    }

    public Object peek() {
        return stack.peek();
    }
}
