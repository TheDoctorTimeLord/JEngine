package ru.jengine.beancontainer.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reflections.ReflectionUtils;

import ru.jengine.beancontainer.InterfaceLocator;

public class InterfaceLocatorByResolver implements InterfaceLocator {
    private final Map<Class<?>, List<Class<?>>> beansByInterface = new ConcurrentHashMap<>();
    private final Function<Class<?>, Object> beanResolver;

    public InterfaceLocatorByResolver(Function<Class<?>, Object> beanResolver) {
        this.beanResolver = beanResolver;
    }

    @Override
    public void registerClassInterfaces(Class<?> cls) {
        if (cls.isInterface()) {
            return; //TODO исправить добавление интерфейса
        }

        ReflectionUtils.getAllSuperTypes(cls, Class::isInterface).forEach(interfaceClass -> {
            if (!beansByInterface.containsKey(interfaceClass)) {
                beansByInterface.put(interfaceClass, new ArrayList<>());
            }
            beansByInterface.get(interfaceClass).add(cls);
        });
    }

    @Override
    public List<Class<?>> getAllImplementedClasses(Class<?> interfaceClass) {
        return beansByInterface.getOrDefault(interfaceClass, Collections.emptyList());
    }

    @Override
    public List<Object> getAllImplementations(Class<?> interfaceClass) {
        return beansByInterface.getOrDefault(interfaceClass, Collections.emptyList())
                .stream()
                .map(beanResolver)
                .collect(Collectors.toList());
    }
}
