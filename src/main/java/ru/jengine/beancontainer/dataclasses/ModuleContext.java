package ru.jengine.beancontainer.dataclasses;

import ru.jengine.beancontainer.ClassFinder;

public class ModuleContext {
    private final ClassFinder classFinder;

    public ModuleContext(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    public ClassFinder getClassFinder() {
        return classFinder;
    }
}
