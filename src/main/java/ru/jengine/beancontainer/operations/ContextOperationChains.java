package ru.jengine.beancontainer.operations;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.operations.defaultimpl.ContainerMetainfoRegistrarOperation;
import ru.jengine.beancontainer.operations.defaultimpl.FinishingInitializeContextOperation;
import ru.jengine.beancontainer.operations.defaultimpl.ModuleFinderOperation;
import ru.jengine.beancontainer.operations.defaultimpl.PreloadContextOperation;
import ru.jengine.beancontainer.operations.defaultimpl.PresetContextFacadeOperation;
import ru.jengine.beancontainer.operations.defaultimpl.StartingInitializeContextOperation;

public class ContextOperationChains
{
    public static final ContainerOperation[] INITIALIZE_CONTAINER = {
            new ModuleFinderOperation(),
            new PreloadContextOperation(Constants.Contexts.INFRASTRUCTURE_CONTEXT),
            new PreloadContextOperation(Constants.Contexts.EXTERNAL_BEANS_CONTEXT),
            new PresetContextFacadeOperation(),
            new ContainerMetainfoRegistrarOperation(),
            new StartingInitializeContextOperation(),
            new FinishingInitializeContextOperation()
    };
}
