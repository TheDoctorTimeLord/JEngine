package ru.jengine.beancontainer.intstructure.pac8;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = "1")
@Import(N.class)
public class OtherModule extends AnnotationModule {
}
