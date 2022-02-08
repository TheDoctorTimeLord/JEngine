package ru.jengine.battlemodule.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.service.Constants.Contexts;
import ru.jengine.taskscheduler.EnableTaskScheduler;

@ContainerModule
@Context(value = Contexts.BATTLE_CONTEXT, beanSources = Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.battlemodule.core")
@EnableTaskScheduler
public class CoreBattleModule extends AnnotationModule {
}
