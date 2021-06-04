package ru.jengine.beancontainer.dataclasses;

import ru.jengine.beancontainer.ClassFinder;

public class ModuleContext {
    private final ClassFinder classFinder;
    private final Class<?> moduleClass;

    public ModuleContext(ClassFinder classFinder, Class<?> moduleClass) {
        this.classFinder = classFinder;
        this.moduleClass = moduleClass;
    }

    public ClassFinder getClassFinder() {
        return classFinder;
    }

    public Class<?> getModuleClass() {
        return moduleClass;
    }
}
