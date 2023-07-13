package ru.test.annotation.battle.infrastructure;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationInfrastructureModule;

@ContainerModule(contextName = Contexts.INFRASTRUCTURE_CONTEXT)
@PackageScan("ru.test.annotation.battle.infrastructure")
public class TestInfrastructureModule extends AnnotationInfrastructureModule {
}
