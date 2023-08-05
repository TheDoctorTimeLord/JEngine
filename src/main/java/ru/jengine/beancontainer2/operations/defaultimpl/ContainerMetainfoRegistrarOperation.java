package ru.jengine.beancontainer2.operations.defaultimpl;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.EmptyOperationResult;
import ru.jengine.beancontainer2.operations.OperationResult;
import ru.jengine.beancontainer2.operations.defaultimpl.ModuleFinderOperation.OperationResultWithModules;

import java.util.List;
import java.util.Map;

public class ContainerMetainfoRegistrarOperation implements ContainerOperation<OperationResultWithModules> {
    @Override
    public OperationResult apply(OperationResultWithModules beforeOperationResult, ContainerState context) {
        ContainerConfiguration containerConfiguration = context.getContainerConfiguration();
        ContextMetainfoFactory contextMetainfoFactory = containerConfiguration.getContextMetainfoFactory();
        ContextMetainfoManager contextMetainfoManager = context.getContextMetainfoManager();

        for (Map.Entry<String, List<Module>> entry : beforeOperationResult.modulesByContext().entrySet()) {
            ContextMetainfo contextMetainfo =
                    contextMetainfoFactory.build(entry.getKey(), entry.getValue(), containerConfiguration);

            contextMetainfoManager.registerContextMetainfo(entry.getKey(), contextMetainfo);
        }

        return EmptyOperationResult.INSTANCE;
    }
}
