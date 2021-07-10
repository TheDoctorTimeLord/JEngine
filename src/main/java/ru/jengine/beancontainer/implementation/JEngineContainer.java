package ru.jengine.beancontainer.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ru.jengine.beancontainer.BeanContainer;
import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.ModuleFindersHandler;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.implementation.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.implementation.contexts.DefaultContainerContext;
import ru.jengine.beancontainer.implementation.factories.AutowireBeanFactory;
import ru.jengine.beancontainer.implementation.factories.AutowireConfigurableBeanFactory;
import ru.jengine.beancontainer.implementation.factories.StubBeanFactory;
import ru.jengine.beancontainer.implementation.modulefinders.SyntheticModuleFinder;
import ru.jengine.beancontainer.implementation.moduleimpls.ExistBeansModule;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

public class JEngineContainer implements BeanContainer {
    private ContainerMultiContext beanContainerContext;

    @Override
    public void initialize(ContainerConfiguration configuration) {
        List<Module> modules = findModules(configuration);
        prepareContext(modules);
    }

    private static List<Module> findModules(ContainerConfiguration configuration) {
        ModuleFindersHandler moduleFindersHandler = new ModuleFindersHandler();
        SyntheticModuleFinder syntheticModuleFinder = new SyntheticModuleFinder();

        syntheticModuleFinder.addModuleClass(Constants.BEAN_CONTAINER_MAIN_INFRASTRUCTURE_MODULE);
        syntheticModuleFinder.addModuleClass(Constants.BEAN_CONTAINER_MAIN_MODULE);
        syntheticModuleFinder.addModuleClass(configuration.getMainModuleClass());

        List<Module> result = moduleFindersHandler.findAllModules(syntheticModuleFinder, configuration);
        result.add(new ExistBeansModule(configuration.getAdditionalBeans()));

        return result;
    }

    private void prepareContext(List<Module> modules) {
        List<Module> infrastructureModules = filter(modules, JEngineContainer::isInfrastructureModule);
        ContainerContext infrastructureContext = createInfrastructureContext(infrastructureModules);

        List<Module> existingModules = filter(modules, JEngineContainer::isExistingModule);
        ContainerContext existingContext = createExistingContext(existingModules);

        beanContainerContext = new ContainerContextFacade();
        beanContainerContext.registerContext(Constants.INFRASTRUCTURE_CONTEXT, infrastructureContext);
        beanContainerContext.registerContext(Constants.EXISTING_CONTEXT, existingContext);

        BeanContext preProcessors = infrastructureContext.getBean(ContextPreProcessor.class);

        BeanFactory factory = createBeanFactory(beanContainerContext, infrastructureContext);
        beanContainerContext.initialize(modules, factory);
        beanContainerContext.preProcessBeans(BeanUtils.getBeanAsList(preProcessors));
        beanContainerContext.prepareBeans();
    }

    private static List<Module> filter(List<Module> modules, Predicate<Module> filter) {
        List<Module> filtered = new ArrayList<>();

        modules.removeIf(module -> {
            if (filter.test(module)) {
                filtered.add(module);
                return true;
            }
            return false;
        });

        return filtered;
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

    private static ContainerContext createExistingContext(List<Module> modules) {
        ContainerContext existingContext = new DefaultContainerContext();
        existingContext.initialize(modules, new StubBeanFactory());
        existingContext.prepareBeans();
        return existingContext;
    }

    private static boolean isExistingModule(Module module) {
        return module instanceof ExistBeansModule;
    }

    private static BeanFactory createBeanFactory(ContainerContext mainContext, ContainerContext infrastructureContext) {
        AutowireConfigurableBeanFactory factory = new AutowireConfigurableBeanFactory(mainContext);
        factory.configure(infrastructureContext);
        return factory;
    }

    @Override
    public <T> T getBean(Class<?> beanClass) {
        return beanContainerContext.getBean(beanClass).getBean();
    }

    @Override
    public void stop() {
        beanContainerContext.prepareToRemove();
    }

    @Override
    public void registerContext(String name, ContainerContext context) {
        beanContainerContext.registerContext(name, context);
    }

    @Override
    public void reloadContext(String name) {
        beanContainerContext.reloadContext(name);
    }

    @Override
    public void removeContext(String name) {
        beanContainerContext.removeContext(name);
    }
}
