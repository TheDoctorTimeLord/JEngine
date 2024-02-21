package ru.jengine.beancontainer.operations;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.operations.defaultimpl.*;

public class ContextOperationChains
{
    public static final ContainerOperation[] INITIALIZE_CONTAINER = {
            new ModuleFinderOperation(),
            new PreloadContextOperation(Constants.Contexts.INFRASTRUCTURE_CONTEXT),
            new PreloadContextOperation(Constants.Contexts.EXTERNAL_BEANS_CONTEXT),
            new ContainerStateToInfrastructureProvider(),
            new PresetContextFacadeOperation(),
            new ContainerMetainfoRegistrarOperation(),
            new StartingInitializeContextOperation(),
            new FinishingInitializeContextOperation()
    };
}
