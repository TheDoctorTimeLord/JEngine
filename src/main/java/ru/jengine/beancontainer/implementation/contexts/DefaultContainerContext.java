package ru.jengine.beancontainer.implementation.contexts;

import ru.jengine.beancontainer.*;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.implementation.InterfaceLocatorByResolver;
import ru.jengine.beancontainer.utils.ClassUtils;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultContainerContext implements ContainerContext {
    private BeanFactory beanFactory;
    private List<BeanDefinition> beanDefinitions;
    private final Map<Class<?>, BeanContext> beans = new ConcurrentHashMap<>();
    private final InterfaceLocator interfaceLocator = new InterfaceLocatorByResolver(cls -> getBean(cls).getBean());

    @Override
    public void initialize(List<Module> modules, BeanFactory factory) {
        this.beanFactory = factory;
        this.beanDefinitions = ContainerModuleUtils.extractAllBeanDefinition(modules);
        beanDefinitions.forEach(this::registerDefinition);
    }

    private void registerDefinition(BeanDefinition definition) {
        Class<?> beanClass = definition.getBeanClass();

        beans.put(beanClass, new BeanContext(beanClass));
        interfaceLocator.registerClassInterfaces(beanClass);
    }

    @Override
    public void preProcessBeans(List<ContextPreProcessor> contextPreProcessors) {
        beanDefinitions.removeIf(beanDefinition -> {
            contextPreProcessors.forEach(preProcessor -> preProcessor.preProcess(beanDefinition));
            return beanDefinition.mustRemovedAfterPreProcess();
        });
    }

    @Override
    public void prepareBeans() {
        prepareSingletons();
    }

    private void prepareSingletons() {
        beanDefinitions.stream()
                .filter(BeanDefinition::isSingleton)
                .forEach(beanDefinition -> getBean(beanDefinition.getBeanClass()));
    }

    @Override
    public BeanContext getBean(Class<?> beanClass) {
        return Collection.class.isAssignableFrom(beanClass)
                ? getCollectionBeans(beanClass)
                : getBeanCommonOrInterfaceBean(beanClass);
    }

    private BeanContext getBeanCommonOrInterfaceBean(Class<?> beanClass) {
        return beanClass.isInterface()
                ? getImplementationsInterface(beanClass)
                : getCommonBean(beanClass);
    }

    private BeanContext getCollectionBeans(Class<?> beanClass) {
        Class<?> collectionParameter = ClassUtils.getFirstGenericType(beanClass);
        BeanContext beanContext = getBeanCommonOrInterfaceBean(collectionParameter);

        Collection<?> beanInst;
        if (beanContext.isCollectionBean()) {
            beanInst = beanContext.getBean();
            if (List.class.isAssignableFrom(beanClass)) {
                beanInst = new ArrayList<>(beanInst);
            } else if (Set.class.isAssignableFrom(beanClass)) {
                beanInst = new HashSet<>(beanInst);
            } else {
                throw new ContainerException("Collection with bean must be List or Set and is not [" + beanClass + "]");
            }
        } else {
            if (List.class.isAssignableFrom(beanClass)) {
                beanInst = Collections.singletonList(beanContext.getBean());
            } else if (Set.class.isAssignableFrom(beanClass)) {
                beanInst = Collections.singleton(beanContext.getBean());
            } else {
                throw new ContainerException("Collection with bean must be List or Set and is not [" + beanClass + "]");
            }
        }
        beanContext.setInstance(beanInst);

        return beanContext;
    }

    private BeanContext getImplementationsInterface(Class<?> beanClass) {
        List<Object> implementations = interfaceLocator.getAllImplementations(beanClass);
        return implementations.size() == 1
                ? new BeanContext(implementations.get(0), beanClass)
                : new BeanContext(implementations, beanClass);
    }

    private BeanContext getCommonBean(Class<?> beanClass) {
        BeanContext context = beans.get(beanClass);
        if (context == null) {
            return null;
        }
        if (context.getBean() != null) {
            return context;
        }
        BeanContext beanContext = beanFactory.buildBean(beanClass);
        context.setInstance(beanContext.getBean());

        return context;
    }

    @Override
    public boolean containsBean(Class<?> beanClass) {
        return beans.containsKey(beanClass);
    }

    @Override
    public void deleteBean(Object bean) {

    }
}
