package ru.jengine.virtualmachine;

import ru.jengine.virtualmachine.commands.*;
import ru.jengine.virtualmachine.context.ScriptContext;
import ru.jengine.virtualmachine.exceptions.VirtualMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VirtualMachine {

    private final Map<Byte, Command> commandsMap;

    private final List<ScriptContext> contexts = new ArrayList<>();

    public VirtualMachine(List<Command> commands) {
        commandsMap = commands.stream().collect(Collectors.toMap(Command::getOpCode, Function.identity()));
    }

    public void executeRegisteredContexts() {
        for (ScriptContext context : this.contexts) {
            execute(context);
        }
    }

    private Command extractNextCommand(ScriptContext context, byte[] code) {
        byte opCode = code[context.getInstructionPointer()];
        return commandsMap.get(opCode);
    }

    public void registerScript(List<Object> initialStorage, byte[] bytecode) {
        ScriptContext scriptContext = new ScriptContext(initialStorage, bytecode);
        contexts.add(scriptContext);
    }

    private void execute(ScriptContext context) {
        byte[] code = context.getCode();
        while (context.getInstructionPointer() < code.length) {
            Command command = extractNextCommand(context, code);
            try {
                int offset = command.execute(context);
                context.moveInstructionPointer(offset);
            } catch (VirtualMachineException e) {
                throw e; // TODO: ???
            }
        }

    }

}
