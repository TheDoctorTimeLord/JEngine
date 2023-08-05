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
import ru.jengine.beancontainer2.operations.defaultimpl.ModuleFinderOperation.OperationResultWithModules;

import java.util.List;

public class CreateInfrastructureContextOperation implements ContainerOperation<OperationResultWithModules> {
    @Override
    public OperationResult apply(OperationResultWithModules beforeOperationResult, ContainerState context) {
        ContainerConfiguration containerConfiguration = context.getContainerConfiguration();
        ContainerContextFacade containerContextFacade = context.getContainerContextFacade();

        List<Module> infrastructureModules =
                beforeOperationResult.modulesByContext().remove(Contexts.INFRASTRUCTURE_CONTEXT);
        ContextMetainfo infrastructureMetainfo = containerConfiguration.getContextMetainfoFactory().build(
                Contexts.INFRASTRUCTURE_CONTEXT,
                infrastructureModules,
                containerConfiguration
        );
        BeanFactory beanFactory = new DefaultBeanFactory(containerContextFacade);

        containerContextFacade.registerContext(
                Contexts.INFRASTRUCTURE_CONTEXT,
                containerConfiguration.getContainerContextFactory().build(infrastructureMetainfo, beanFactory)
        );

        return beforeOperationResult;
    }
}
