package ru.jengine.beancontainer2.intstructure.pac7;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.annotations.ContainerModule;
import ru.jengine.beancontainer2.annotations.Import;
import ru.jengine.beancontainer2.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@Import(J.class)
public class StartModuleWithExistingBeans extends AnnotationModule {
}
