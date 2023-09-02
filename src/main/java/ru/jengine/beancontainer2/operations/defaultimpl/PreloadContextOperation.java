package ru.jengine.beancontainer2.operations.defaultimpl;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.beanfactory.DefaultBeanFactory;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.OperationResult;
import ru.jengine.beancontainer2.operations.ResultConstants;

import java.util.List;
import java.util.Map;

public class PreloadContextOperation extends ContainerOperation {
    private final String contextName;

    public PreloadContextOperation(String contextName) {
        this.contextName = contextName;
    }

    @Override
    public void apply(OperationResult result, ContainerState state) {
        Map<String, List<Module>> modulesByContext = getResult(result, ResultConstants.MODULES_BY_CONTEXT, Map.class);

        ContainerConfiguration containerConfiguration = state.getContainerConfiguration();
        ContainerContextFacade containerContextFacade = state.getContainerContextFacade();

        List<Module> contextModules = modulesByContext.remove(contextName);
        ContextMetainfo contextMetainfo = containerConfiguration.getContextMetainfoFactory().build(
                contextName,
                contextModules != null ? contextModules : List.of(),
                containerConfiguration
        );
        BeanFactory beanFactory = new DefaultBeanFactory(containerContextFacade);

        containerContextFacade.registerContext(
                contextName,
                containerConfiguration.getContainerContextFactory().build(contextName, contextMetainfo, beanFactory, state)
        );
    }
}
