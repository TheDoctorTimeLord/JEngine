package ru.jengine.beancontainer2.operations.defaultimpl;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.events.FinishingInitializeContextsPhase;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.OperationResult;

public class FinishingInitializeContextOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult previouseOperationResult, ContainerState state) {
        state.getContainerStatePublisher().publish(new FinishingInitializeContextsPhase(), state);
    }
}
