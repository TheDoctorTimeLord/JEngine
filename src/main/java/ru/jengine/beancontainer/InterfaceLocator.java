package ru.jengine.beancontainer;

import java.util.List;

public interface InterfaceLocator {
    void registerClassInterfaces(Class<?> cls);
    List<Class<?>> getAllImplementedClasses(Class<?> interfaceClass);
    List<Object> getAllImplementations(Class<?> interfaceClass);
}
