package ru.jengine.beancontainer;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.annotations.PackagesScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@PackagesScan({
        @PackageScan("ru.jengine.beancontainer"),
        @PackageScan("ru.jengine.eventqueue")
})
public class MainModule extends AnnotationModule {
}
