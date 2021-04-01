package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.*;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.implementation.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.implementation.contexts.DefaultContainerContext;
import ru.jengine.beancontainer.implementation.factories.AutowireBeanFactory;
import ru.jengine.beancontainer.implementation.factories.AutowireConfigurableBeanFactory;
import ru.jengine.beancontainer.implementation.modulefinders.SyntheticModuleFinder;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanContainerImpl implements BeanContainer {
    private ContainerMultiContext beanContainerContext;

    @Override
    public void initialize(Class<?> mainModule, Object... additionalBeans) {
        List<Module> modules = findModules(mainModule);
        prepareContext(modules);
    }

    private static List<Module> findModules(Class<?> mainModule) {
        ModuleFindersHandler moduleFindersHandler = new ModuleFindersHandler();
        SyntheticModuleFinder syntheticModuleFinder = new SyntheticModuleFinder();

        syntheticModuleFinder.addModuleClass(Constants.BEAN_CONTAINER_MAIN_MODULE);
        syntheticModuleFinder.addModuleClass(mainModule);

        return moduleFindersHandler.findAllModules(syntheticModuleFinder, new ContainerConfiguration());
    }

    private void prepareContext(List<Module> modules) {
        List<Module> infrastructureModules = modules.stream()
                .filter(BeanContainerImpl::isInfrastructureModule)
                .collect(Collectors.toList());
        ContainerContext infrastructureContext = createInfrastructureContext(infrastructureModules);

        List<Module> otherModules = modules.stream()
                .filter(module -> !isInfrastructureModule(module))
                .collect(Collectors.toList());

        BeanContext postProcessors = infrastructureContext.getBean(ContextPreProcessor.class);

        beanContainerContext = new ContainerContextFacade();
        beanContainerContext.registerContext(Constants.INFRASTRUCTURE_CONTEXT, infrastructureContext);

        BeanFactory factory = createBeanFactory(beanContainerContext, infrastructureContext);
        beanContainerContext.initialize(otherModules, factory);
        beanContainerContext.preProcessBeans(BeanUtils.getBeanAsList(postProcessors));
        beanContainerContext.prepareBeans();
    }

    private static ContainerContext createInfrastructureContext(List<Module> modules) {
        ContainerContext infrastructureContext = new DefaultContainerContext();
        infrastructureContext.initialize(modules, new AutowireBeanFactory(infrastructureContext));
        infrastructureContext.prepareBeans();
        return infrastructureContext;
    }

    private static boolean isInfrastructureModule(Module module) {
        return Constants.INFRASTRUCTURE_CONTEXT.equals(ContainerModuleUtils.extractContextForModule(module));
    }

    private static BeanFactory createBeanFactory(ContainerContext mainContext, ContainerContext infrastructureContext) {
        AutowireConfigurableBeanFactory factory = new AutowireConfigurableBeanFactory(mainContext);
        factory.configure(infrastructureContext);
        return factory;
    }
}
