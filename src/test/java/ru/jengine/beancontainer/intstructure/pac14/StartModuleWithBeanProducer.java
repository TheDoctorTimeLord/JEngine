package ru.jengine.beancontainer.intstructure.pac14;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.beancontainer.intstructure.pac14.")
public class StartModuleWithBeanProducer extends AnnotationModule {
    @Bean
    public Z produce(Y y) {
        return new Z(y);
    }
}
