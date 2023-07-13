package ru.jengine.beancontainer.operations.special;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;

public class RemoveContextOperation extends ContainerOperation {
    private String removedContextName;

    public RemoveContextOperation(String removedContextName) {
        this.removedContextName = removedContextName;
    }

    @Override
    public void apply(OperationResult previouseOperationResult, ContainerState state) {
        state.getContainerContextFacade().removeContext(removedContextName);
    }
}
