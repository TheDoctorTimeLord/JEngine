package ru.jengine.beancontainer2.intstructure.pac1;

import ru.jengine.beancontainer2.annotations.ContainerModule;
import ru.jengine.beancontainer2.annotations.ImportModule;
import ru.jengine.beancontainer2.intstructure.pac2.ModuleInOtherPackage;
import ru.jengine.beancontainer2.modules.AnnotationModule;

@ContainerModule(contextName = "1")
@ImportModule(ModuleInOtherPackage.class)
public class Module1 extends AnnotationModule {
}
