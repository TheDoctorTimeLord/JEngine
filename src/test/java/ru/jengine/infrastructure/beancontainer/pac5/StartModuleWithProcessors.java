package ru.jengine.infrastructure.beancontainer.pac5;

import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.beancontainer.intstructure.pac5.")
public class StartModuleWithProcessors extends AnnotationModule {
}
