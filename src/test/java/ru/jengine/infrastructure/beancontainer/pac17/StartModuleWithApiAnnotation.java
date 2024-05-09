package ru.jengine.infrastructure.beancontainer.pac17;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@Import(AG.class)
public class StartModuleWithApiAnnotation extends AnnotationModule {
}
