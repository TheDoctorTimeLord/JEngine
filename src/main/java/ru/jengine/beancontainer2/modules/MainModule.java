package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.Constants.Contexts;
import ru.jengine.beancontainer2.annotations.ContainerModule;
import ru.jengine.beancontainer2.annotations.PackageScan;
import ru.jengine.beancontainer2.annotations.PackagesScan;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@PackagesScan({
        @PackageScan("ru.jengine.beancontainer2")
})
public class MainModule extends AnnotationModule {
}
