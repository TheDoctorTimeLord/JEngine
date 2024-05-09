package ru.jengine.infrastructure.beancontainer.pac11;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "module1", beanSources = Constants.Contexts.DEFAULT_CONTEXT)
@Import(Module1Bean.class)
public class Module1 extends AnnotationModule {
}
