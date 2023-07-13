package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.classfinders.ClassFinder;

public record ModuleContext(ClassFinder classFinder, Class<?> moduleClass) { }
