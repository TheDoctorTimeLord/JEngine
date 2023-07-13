package ru.jengine.beancontainer.intstructure.pac_11;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "module3", beanSources = { "module2", Constants.Contexts.DEFAULT_CONTEXT })
@Import(Module3Bean.class)
public class Module3 extends AnnotationModule {
}
