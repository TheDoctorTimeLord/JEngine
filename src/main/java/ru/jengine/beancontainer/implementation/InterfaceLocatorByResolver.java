package ru.jengine.beancontainer.implementation;

import org.reflections.ReflectionUtils;
import ru.jengine.beancontainer.InterfaceLocator;
import ru.jengine.beancontainer.dataclasses.BeanHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InterfaceLocatorByResolver implements InterfaceLocator {
    private final Map<Class<?>, List<BeanHolder>> beansByInterface = new ConcurrentHashMap<>();
    private final Function<Class<?>, Object> beanResolver;

    public InterfaceLocatorByResolver(Function<Class<?>, Object> beanResolver) {
        this.beanResolver = beanResolver;
    }

    @Override
    public void registerClassInterfaces(Class<?> cls) {
        if (cls.isInterface()) {
            return; //TODO исправить добавление интерфейса
        }
        BeanHolder holder = new BeanHolder(cls);
        ReflectionUtils.getAllSuperTypes(cls, Class::isInterface).forEach(interfaceClass -> {
            if (!beansByInterface.containsKey(interfaceClass)) {
                beansByInterface.put(interfaceClass, new ArrayList<>());
            }
            beansByInterface.get(interfaceClass).add(holder);
        });
    }

    @Override
    public List<Class<?>> getAllImplementedClasses(Class<?> interfaceClass) {
        List<BeanHolder> implementationHolders = beansByInterface.getOrDefault(interfaceClass, Collections.emptyList());

        return implementationHolders.stream()
                .map(BeanHolder::getBeanClass)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> getAllImplementations(Class<?> interfaceClass) {
        List<BeanHolder> implementationHolders = beansByInterface.getOrDefault(interfaceClass, Collections.emptyList());
        implementationHolders.stream()
                .filter(holder -> !holder.wasInitialized())
                .forEach(holder -> {
                    Object bean = beanResolver.apply(holder.getBeanClass());
                    holder.setBean(bean);
                });

        return implementationHolders.stream()
                .map(BeanHolder::getBean)
                .collect(Collectors.toList());
    }
}
