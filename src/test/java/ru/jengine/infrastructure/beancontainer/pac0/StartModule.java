package ru.jengine.infrastructure.beancontainer.pac0;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "1")
@PackageScan("ru.jengine.beancontainer.intstructure.pac1.")
public class StartModule extends AnnotationModule {
}
