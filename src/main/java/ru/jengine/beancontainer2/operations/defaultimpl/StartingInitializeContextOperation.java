package ru.jengine.beancontainer2.operations.defaultimpl;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.events.StartingInitializeContextsPhase;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.OperationResult;

public class StartingInitializeContextOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult previouseOperationResult, ContainerState state) {
        state.getContainerStatePublisher().publish(new StartingInitializeContextsPhase(), state);
    }
}
