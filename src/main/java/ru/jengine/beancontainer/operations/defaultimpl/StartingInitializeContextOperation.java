package ru.jengine.beancontainer.operations.defaultimpl;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.events.StartingInitializeContextsPhase;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;

public class StartingInitializeContextOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult previouseOperationResult, ContainerState state) {
        state.getContainerEventDispatcher().publish(new StartingInitializeContextsPhase(), state);
    }
}
