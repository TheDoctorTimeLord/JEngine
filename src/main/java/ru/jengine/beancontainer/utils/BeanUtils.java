package ru.jengine.beancontainer.utils;

import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.beancontainer.exceptions.ContainerException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BeanUtils {
    public static Object createObjectWithDefaultConstructor(Class<?> cls) {
        try {
            Constructor<?> defaultConstructor = cls.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ContainerException(String.format("%s has not constructor without parameters", cls), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ContainerException(String.format("There was a problem creating the object %s", cls), e);
        }
    }

    public static <T> T createComponentWithDefaultConstructor(Class<?> cls) {
        try {
            return (T) createObjectWithDefaultConstructor(cls);
        } catch (ClassCastException e) {
            throw new ContainerException(String.format("%s can not be cast", cls), e);
        }
    }

    public static Constructor<?> findAppropriateConstructor(Class<?> cls) {
        List<Constructor<?>> constructors = Arrays.asList(cls.getConstructors());

        if (constructors.size() > 1) {
            constructors = constructors.stream()
                    .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                    .collect(Collectors.toList());

            if (constructors.size() > 1) {
                throw new ContainerException("%s has more then 1 constructor annotated Inject");
            }
        }
        if (constructors.size() == 1) {
            return constructors.get(0);
        }
        throw new ContainerException(String.format("%s has no available constructor", cls));
    }

    public static Object createObject(Constructor<?> constructor, Object[] args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ContainerException(String.format("Exception during create object %s", constructor.getDeclaringClass()), e);
        }
    }
}
