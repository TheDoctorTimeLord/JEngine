package ru.jengine.beancontainer.utils;

import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.exceptions.InvocationMethodException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
        List<Constructor<?>> constructors = Arrays.asList(cls.getConstructors());

        if (constructors.size() > 1) {
            constructors = constructors.stream()
                    .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                    .collect(Collectors.toList());

            if (constructors.size() > 1) {
                throw new ContainerException("[" + cls + "] has more then 1 constructor annotated Inject");
            }
        }
        if (constructors.size() == 1) {
            return constructors.get(0);
        }
        throw new ContainerException("[" + cls + "] has no available constructor");
    }

    public static Object createObject(Constructor<?> constructor, Object[] args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ContainerException("Exception during create object [" + constructor.getDeclaringClass() + "]", e);
        }
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

    public static <T> List<T> getBeanAsList(BeanContext context) {
        if (context.isCollectionBean()) {
            Collection<T> collection = context.getBean();
            return collection instanceof List ? (List<T>) collection : new ArrayList<>(collection);
        } else {
            return Collections.singletonList(context.getBean());
        }
    }
}
