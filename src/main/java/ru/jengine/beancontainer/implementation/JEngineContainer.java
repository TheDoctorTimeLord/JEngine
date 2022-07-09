package ru.jengine.beancontainer.implementation;

import static ru.jengine.utils.CollectionUtils.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.jengine.beancontainer.BeanContainer;
import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.ContextPattern;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.InitializableContextPatternHandler;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.ModuleFindersHandler;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.implementation.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.implementation.contexts.DefaultContainerContext;
import ru.jengine.beancontainer.implementation.contexts.patterns.DefaultContextPatternsHandler;
import ru.jengine.beancontainer.implementation.contexts.patterns.ModuleBasedContextPattern;
import ru.jengine.beancontainer.implementation.factories.AutowireBeanFactory;
import ru.jengine.beancontainer.implementation.factories.AutowireConfigurableBeanFactory;
import ru.jengine.beancontainer.implementation.modulefinders.SyntheticModuleFinder;
import ru.jengine.beancontainer.implementation.moduleimpls.ExistBeansModule;
import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.utils.CollectionUtils;

public class JEngineContainer implements BeanContainer {
    private final ContainerMultiContext beanContainerContext = new ContainerContextFacade();
    private final InitializableContextPatternHandler patternsHandler =
            new DefaultContextPatternsHandler(beanContainerContext);

    @Override
    public void initializeCommonContexts(ContainerConfiguration configuration) {
        List<Module> modules = findModules(configuration);
        prepareContext(createInfrastructureContext(filter(modules, JEngineContainer::isInfrastructureModule)));
        loadFoundedContexts(modules);
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

    private void prepareContext(ContainerContext infrastructureContext) {
        beanContainerContext.registerContext(Contexts.INFRASTRUCTURE_CONTEXT, infrastructureContext);

        BeanContext preProcessors = infrastructureContext.getBean(ContextPreProcessor.class);

        BeanFactory factory = createBeanFactory(beanContainerContext, infrastructureContext);
        beanContainerContext.initialize(Collections.emptyList(), factory); //TODO что делать с модулями тут?
        beanContainerContext.preProcessBeans(BeanUtils.getBeanAsList(preProcessors));
        beanContainerContext.prepareBeans();
    }

    private static ContainerContext createInfrastructureContext(List<Module> modules) {
        ContainerContext infrastructureContext = new DefaultContainerContext();
        infrastructureContext.initialize(modules, new AutowireBeanFactory(infrastructureContext));
        infrastructureContext.prepareBeans();
        return infrastructureContext;
    }

    private static boolean isInfrastructureModule(Module module) {
        return Contexts.INFRASTRUCTURE_CONTEXT.equals(module.getContextName());
    }

    private static BeanFactory createBeanFactory(ContainerContext mainContext, ContainerContext infrastructureContext) {
        AutowireConfigurableBeanFactory factory = new AutowireConfigurableBeanFactory(mainContext);
        factory.configure(infrastructureContext);
        return factory;
    }

    private static Map<String, ContextPattern> groupModuleToPatterns(List<Module> groupingModules) {
        Map<String, ContextPattern> result = new HashMap<>();
        CollectionUtils.groupBy(groupingModules, Module::getContextName)
                .forEach((name, modules) -> result.put(name, new ModuleBasedContextPattern(modules)));
        return result;
    }

    private void loadFoundedContexts(List<Module> modules) {
        groupModuleToPatterns(modules).forEach(patternsHandler::registerPattern);
        patternsHandler.initialize();
    }

    @Override
    public <T> T getBean(Class<? extends T> beanClass) {
        BeanContext bean = beanContainerContext.getBean(beanClass);
        return (T)BeanUtils.getBean(bean);
    }

    @Override
    public <T> T getBean(String contextName, Class<? extends T> beanClass) {
        BeanContext bean = beanContainerContext.getBean(contextName, beanClass);
        return (T)BeanUtils.getBean(bean);
    }

    @Override
    public void stop() {
        beanContainerContext.prepareToRemove();
    }

    @Override
    public void registerPattern(String patternName, ContextPattern contextPattern) throws ContainerException {
        patternsHandler.registerPattern(patternName, contextPattern);
    }

    @Override
    public void loadContext(String patternName) {
        patternsHandler.loadContext(patternName);
    }

    @Override
    public void loadContexts(List<String> patternNames) {
        patternsHandler.loadContexts(patternNames);
    }

    @Override
    public void loadCopiedContext(String copiedPatternName, String loadedPatternName) {
        patternsHandler.loadCopiedContext(copiedPatternName, loadedPatternName);
    }
}
