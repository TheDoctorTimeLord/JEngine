package ru.jengine.beancontainer.intstructure.pac_11;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "module2", beanSources = { "module1", Constants.Contexts.DEFAULT_CONTEXT })
@Import(Module2Bean.class)
public class Module2 extends AnnotationModule {
}
