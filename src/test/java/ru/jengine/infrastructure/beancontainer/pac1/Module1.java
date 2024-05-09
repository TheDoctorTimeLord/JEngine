package ru.jengine.infrastructure.beancontainer.pac1;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.ImportModule;
import ru.jengine.infrastructure.beancontainer.pac2.ModuleInOtherPackage;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "1")
@ImportModule(ModuleInOtherPackage.class)
public class Module1 extends AnnotationModule {
}
