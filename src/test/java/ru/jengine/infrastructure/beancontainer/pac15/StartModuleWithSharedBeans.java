package ru.jengine.infrastructure.beancontainer.pac15;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.ImportModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.infrastructure.beancontainer.pac16.AnotherModule;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@ImportModule(AnotherModule.class)
@PackageScan("ru.jengine.beancontainer.intstructure.pac15")
public class StartModuleWithSharedBeans extends AnnotationModule {
}
