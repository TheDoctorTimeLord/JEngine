package ru.jengine.beancontainer.intstructure.pac16;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "1")
@PackageScan("ru.jengine.beancontainer.intstructure.pac16")
@Import(AF.class)
public class AnotherModule extends AnnotationModule {
}
