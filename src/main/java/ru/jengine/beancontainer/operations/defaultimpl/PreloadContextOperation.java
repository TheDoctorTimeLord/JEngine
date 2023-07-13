package ru.jengine.beancontainer.operations.defaultimpl;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.beanfactory.DefaultBeanFactory;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer.modules.Module;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;
import ru.jengine.beancontainer.operations.ResultConstants;

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
