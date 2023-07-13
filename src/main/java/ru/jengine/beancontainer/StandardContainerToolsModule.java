package ru.jengine.beancontainer;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.ImportModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.eventqueue")
@ImportModule(StandardContainerInfrastructureToolsModule.class)
public class StandardContainerToolsModule extends AnnotationModule {
}
