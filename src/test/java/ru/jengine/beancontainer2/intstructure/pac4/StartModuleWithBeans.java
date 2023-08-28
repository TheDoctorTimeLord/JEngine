package ru.jengine.beancontainer2.intstructure.pac4;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.annotations.ContainerModule;
import ru.jengine.beancontainer2.annotations.PackageScan;
import ru.jengine.beancontainer2.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.beancontainer2.intstructure.pac4")
public class StartModuleWithBeans extends AnnotationModule {
}
