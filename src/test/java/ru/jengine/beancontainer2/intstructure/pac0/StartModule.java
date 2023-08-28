package ru.jengine.beancontainer2.intstructure.pac0;

import ru.jengine.beancontainer2.annotations.ContainerModule;
import ru.jengine.beancontainer2.annotations.PackageScan;
import ru.jengine.beancontainer2.modules.AnnotationModule;

@ContainerModule(contextName = "1")
@PackageScan("ru.jengine.beancontainer2.intstructure.pac1")
public class StartModule extends AnnotationModule {
}
