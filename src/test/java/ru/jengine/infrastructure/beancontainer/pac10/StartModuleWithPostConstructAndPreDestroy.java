package ru.jengine.infrastructure.beancontainer.pac10;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.infrastructure.beancontainer.pac10.")
public class StartModuleWithPostConstructAndPreDestroy extends AnnotationModule {
}
