package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.classfinders.ClassFinder;

public record ModuleContext(ClassFinder classFinder, Class<?> moduleClass) { }
