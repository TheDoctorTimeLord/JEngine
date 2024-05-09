package ru.jengine.infrastructure.beancontainer.pac11;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.infrastructure.beancontainer.pac11.")
@Import(RemoveCounter.class)
public class StartModuleWithModuleHierarchy extends AnnotationModule {
}
