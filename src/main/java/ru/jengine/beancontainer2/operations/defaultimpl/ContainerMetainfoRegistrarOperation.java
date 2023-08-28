package ru.jengine.beancontainer2.operations.defaultimpl;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.OperationResult;
import ru.jengine.beancontainer2.operations.ResultConstants;

import java.util.List;
import java.util.Map;

public class ContainerMetainfoRegistrarOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult result, ContainerState state) {
        Map<String, List<Module>> modulesByContext = poolResult(result, ResultConstants.MODULES_BY_CONTEXT, Map.class);

        ContainerConfiguration containerConfiguration = state.getContainerConfiguration();
        ContextMetainfoFactory contextMetainfoFactory = containerConfiguration.getContextMetainfoFactory();
        ContextMetainfoManager contextMetainfoManager = state.getContextMetainfoManager();

        for (Map.Entry<String, List<Module>> entry : modulesByContext.entrySet()) {
            ContextMetainfo contextMetainfo =
                    contextMetainfoFactory.build(entry.getKey(), entry.getValue(), containerConfiguration);

            contextMetainfoManager.registerContextMetainfo(entry.getKey(), contextMetainfo);
        }
    }
}
