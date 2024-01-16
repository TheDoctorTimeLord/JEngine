package ru.jengine.virtualmachine;

import ru.jengine.virtualmachine.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Object> firstStorage = new ArrayList<>(Arrays.asList(5, 7, 2));
        byte[] firstCode = {
                0b00,
                0,
                0b00,
                1,
                0b100,
                0b00,
                2,
                0b01
        };
        VirtualMachine virtualMachine = new VirtualMachine(List.of(new Add(), new Const(), new Sub(), new Div(), new Mul(), new Jump()));
        virtualMachine.registerScript(firstStorage, firstCode);
        virtualMachine.executeRegisteredContexts();

        // TODO: Handle errors?
    }
}