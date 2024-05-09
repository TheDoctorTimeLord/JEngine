package ru.jengine.infrastructure.beancontainer.pac12;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.beancontainer.intstructure.pac12.")
public class StartModuleWithAfterInitializeBeans extends AnnotationModule {
}
