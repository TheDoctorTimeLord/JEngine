package ru.jengine.beancontainer.dataclasses;

import ru.jengine.beancontainer.ClassFinder;

public class ModuleFindContext {
    private final ClassFinder classFinder;

    public ModuleFindContext(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    public ClassFinder getClassFinder() {
        return classFinder;
    }
}
