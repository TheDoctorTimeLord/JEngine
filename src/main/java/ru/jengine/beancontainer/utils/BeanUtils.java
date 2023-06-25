package ru.jengine.beancontainer.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.beancontainer.BeanFactoryStrategy;
import ru.jengine.beancontainer.Constants.BeanStrategy;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.exceptions.InvocationMethodException;
import ru.jengine.beancontainer.implementation.factories.beanstrategies.PrototypeBeanFactoryStrategy;
import ru.jengine.beancontainer.implementation.factories.beanstrategies.SingletonBeanFactoryStrategy;

public class BeanUtils {
    public static Object createObjectWithDefaultConstructor(Class<?> cls) {
        try {
            Constructor<?> defaultConstructor = cls.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ContainerException("[" + cls + "] has not constructor without parameters", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ContainerException("There was a problem creating the object [" + cls + "]", e);
        }
    }

    public static <T> T createComponentWithDefaultConstructor(Class<?> cls) {
        try {
            return (T) createObjectWithDefaultConstructor(cls);
        } catch (ClassCastException e) {
            throw new ContainerException("[" + cls + "] can not be cast", e);
        }
    }

    public static Constructor<?> findAppropriateConstructor(Class<?> cls) {
        List<Constructor<?>> constructors = Arrays.asList(cls.getDeclaredConstructors());

        if (constructors.size() > 1) {
            constructors = constructors.stream()
                    .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                    .collect(Collectors.toList());

            if (constructors.size() > 1) {
                throw new ContainerException("[" + cls + "] has more then 1 constructor annotated Inject");
            }
        }
        if (constructors.size() == 1) {
            Constructor<?> constructor = constructors.get(0);
            constructor.setAccessible(true);
            return constructor;
        }
        throw new ContainerException("[" + cls + "] has no available constructor");
    }

    public static Object createObject(Constructor<?> constructor, Object[] args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ContainerException("Exception during create object [" + constructor.getDeclaringClass() + "]", e);
        }
    }

    public static void invokeSetterBeanMethod(MethodMeta methodMeta) {
        Method invokedMethod = methodMeta.getMethod();
        if (!invokedMethod.isAnnotationPresent(Inject.class)) {
            throw new InvocationMethodException("Method [" + invokedMethod + "] is not setter");
        }
        if (!Void.TYPE.equals(invokedMethod.getReturnType())) {
            throw new InvocationMethodException("Method [" + invokedMethod + "] must not return any objects");
        }

        methodMeta.invokeWithInnerParameters();
    }

    public static void invokePostConstructBeanMethod(MethodMeta methodMeta) {
        Method invokedMethod = methodMeta.getMethod();
        if (!invokedMethod.isAnnotationPresent(PostConstruct.class)) {
            throw new InvocationMethodException("Method [" + invokedMethod + "] is not PostConstruct");
        }
        if (!Void.TYPE.equals(invokedMethod.getReturnType())) {
            throw new InvocationMethodException("Method [" + invokedMethod + "] must not return any objects");
        }
        if (methodMeta.getParameters().length != 0) {
            throw new InvocationMethodException("Method [" + invokedMethod + "] must has no parameters");
        }

        methodMeta.invokeWithInnerParameters();
    }

    public static Object getBean(BeanContext beanContext) {
        return beanContext == null ? null : beanContext.getBean();
    }

    public static <T> List<T> getBeanAsList(BeanContext context) {
        if (context == null) {
            return new ArrayList<>();
        }
        if (context.isCollectionBean()) {
            Collection<T> collection = context.getBean();
            return collection instanceof List ? (List<T>) collection : new ArrayList<>(collection);
        } else {
            return Collections.singletonList(context.getBean());
        }
    }

    public static BeanFactoryStrategy createStrategy(Class<?> cls, String strategyCode) { //TODO подумать о том, как ещё можно создать стратегию
        return switch (strategyCode) {
            case BeanStrategy.SINGLETON -> new SingletonBeanFactoryStrategy(cls);
            case BeanStrategy.PROTOTYPE -> new PrototypeBeanFactoryStrategy(cls);
            default -> throw new ContainerException("Unknown strategy type [" + strategyCode + "]");
        };
    }

    public static BeanContext findAppropriateValueBean(ContainerMultiContext multiContext, Class<?> beanType,
            Collection<String> usedContexts, boolean needAsCollection)
    {
        return needAsCollection
                ? findMultiValueBean(multiContext, beanType, usedContexts)
                : findSingleValueBean(multiContext, beanType, usedContexts);
    }

    private static BeanContext findSingleValueBean(ContainerMultiContext multiContext, Class<?> beanType,
            Collection<String> usedContexts)
    {
        for (String contextName : usedContexts) {
            BeanContext beanContext =
                    checkNotEmptyCollection(multiContext.getBean(contextName, beanType));
            if (beanContext != null) {
                return beanContext;
            }
        }
        return null;
    }

    private static BeanContext findMultiValueBean(ContainerMultiContext multiContext, Class<?> beanType,
            Collection<String> usedContexts)
    {
        return new BeanContext(
                usedContexts.stream()
                        .map(contextName -> multiContext.getBean(contextName, beanType))
                        .filter(Objects::nonNull)
                        .filter(beanContext -> beanContext.getBean() != null)
                        .flatMap(beanContext -> beanContext.isCollectionBean()
                                ? ((Collection<?>)beanContext.getBean()).stream()
                                : Stream.of((Object)beanContext.getBean()))
                        .collect(Collectors.toList()),
                beanType
        );
    }

    private static BeanContext checkNotEmptyCollection(BeanContext beanContext) {
        if (beanContext == null) {
            return null;
        }
        return  beanContext.getBean() instanceof Collection ? null : beanContext;
    }
}
