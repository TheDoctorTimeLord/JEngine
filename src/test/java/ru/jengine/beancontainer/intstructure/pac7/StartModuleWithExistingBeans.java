package ru.jengine.beancontainer.intstructure.pac7;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@Import(J.class)
public class StartModuleWithExistingBeans extends AnnotationModule {
}
