package ru.test.annotation.newp;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.service.Constants;

@ContainerModule
@Context(value = "new", beanSources = Constants.DEFAULT_CONTEXT)
@Import(BeanInNew.class)
public class NewModule extends AnnotationModule {
}
