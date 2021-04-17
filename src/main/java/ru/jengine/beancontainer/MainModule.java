package ru.jengine.beancontainer;

import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.PackagesScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationInfrastructureModule;
import ru.jengine.beancontainer.service.Constants;

@ContainerModule
@Context(Constants.INFRASTRUCTURE_CONTEXT)
@PackagesScan({
        @PackageScan("ru.jengine.beancontainer")
})
public class MainModule extends AnnotationInfrastructureModule {
}
