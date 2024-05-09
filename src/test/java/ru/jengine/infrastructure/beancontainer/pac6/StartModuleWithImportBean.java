package ru.jengine.infrastructure.beancontainer.pac6;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
@Import({F.class, G.class})
public class StartModuleWithImportBean extends AnnotationModule {
}
