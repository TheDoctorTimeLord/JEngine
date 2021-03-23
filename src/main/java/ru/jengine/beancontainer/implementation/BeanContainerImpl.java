package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.*;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanContainerImpl implements BeanContainer {
    private ContainerMultiContext beanContainerContext;

    @Override
    public void initialize(Class<?> mainModule, ClassFinder classFinder, Object... additionalBeans) {
        classFinder.scan("ru.jengine.beancontainer");

        List<Module> modules = findModules(mainModule, classFinder);

        prepareContext(modules);
    }

    private static List<Module> findModules(Class<?> mainModule, ClassFinder classFinder) {
        ModuleFindersHandler moduleFindersHandler = new ModuleFindersHandler(classFinder);
        return moduleFindersHandler.findAllModules(mainModule);
    }

    private void prepareContext(List<Module> modules) {
        List<Module> infrastructureModules = modules.stream()
                .filter(BeanContainerImpl::isInfrastructureModule)
                .collect(Collectors.toList());
        ContainerContext infrastructureContext = createInfrastructureContext(infrastructureModules);

        List<Module> otherModules = modules.stream()
                .filter(module -> !isInfrastructureModule(module))
                .collect(Collectors.toList());

        beanContainerContext = new ContainerContextFacade();
        beanContainerContext.registerContext(Constants.INFRASTRUCTURE_CONTEXT, infrastructureContext);

        BeanFactory factory = createBeanFactory(beanContainerContext, infrastructureContext);
        beanContainerContext.initialize(otherModules, factory);
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
