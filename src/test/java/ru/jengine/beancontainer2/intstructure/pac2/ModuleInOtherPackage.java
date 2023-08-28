package ru.jengine.beancontainer2.intstructure.pac2;

import ru.jengine.beancontainer2.annotations.ContainerModule;
import ru.jengine.beancontainer2.annotations.PackageScan;
import ru.jengine.beancontainer2.modules.AnnotationModule;

@ContainerModule(contextName = "2")
@PackageScan("ru.jengine.beancontainer2.intstructure.pac2")
public class ModuleInOtherPackage extends AnnotationModule {
}
