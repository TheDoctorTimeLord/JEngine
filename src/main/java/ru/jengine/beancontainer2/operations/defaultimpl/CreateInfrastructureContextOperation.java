package ru.jengine.beancontainer2.operations.defaultimpl;

import ru.jengine.beancontainer2.Constants.Contexts;
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

public class CreateInfrastructureContextOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult result, ContainerState state) {
        Map<String, List<Module>> modulesByContext = extractResult(result, ResultConstants.MODULES_BY_CONTEXT, Map.class);

        ContainerConfiguration containerConfiguration = state.getContainerConfiguration();
        ContainerContextFacade containerContextFacade = state.getContainerContextFacade();

        List<Module> infrastructureModules = modulesByContext.remove(Contexts.INFRASTRUCTURE_CONTEXT);
        ContextMetainfo infrastructureMetainfo = containerConfiguration.getContextMetainfoFactory().build(
                Contexts.INFRASTRUCTURE_CONTEXT,
                infrastructureModules,
                containerConfiguration
        );
        BeanFactory beanFactory = new DefaultBeanFactory(containerContextFacade);

        containerContextFacade.registerContext(
                Contexts.INFRASTRUCTURE_CONTEXT,
                containerConfiguration.getContainerContextFactory().build(infrastructureMetainfo, beanFactory, state)
        );
    }
}
