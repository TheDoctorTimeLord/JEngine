package ru.test.annotation.battle.infrastructure;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationInfrastructureModule;
import ru.jengine.beancontainer.service.Constants.Contexts;

@ContainerModule
@Context(Contexts.INFRASTRUCTURE_CONTEXT)
@PackageScan("ru.test.annotation.battle.infrastructure")
public class TestInfrastructureModule extends AnnotationInfrastructureModule {
}
