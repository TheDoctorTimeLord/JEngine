package ru.jengine.beancontainer.intstructure.pac2;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "2")
@PackageScan("ru.jengine.beancontainer.intstructure.pac2.")
public class ModuleInOtherPackage extends AnnotationModule {
}
