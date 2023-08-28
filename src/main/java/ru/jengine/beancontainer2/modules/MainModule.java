package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.Constants.Contexts;
import ru.jengine.beancontainer2.annotations.ContainerModule;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
//@PackagesScan({ TODO в этом есть смысл?
//        @PackageScan("ru.jengine.beancontainer2")
//})
public class MainModule extends AnnotationModule {
}
