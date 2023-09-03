package ru.jengine.battlemodule.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.taskscheduler.EnableTaskScheduler;

/**
 * Модуль, сканирующий пакет основного ядра боевой системы.
 */
@ContainerModule(contextName = Contexts.BATTLE_CONTEXT, beanSources = Contexts.DEFAULT_CONTEXT)
@PackageScan("ru.jengine.battlemodule.core")
@EnableTaskScheduler
public class CoreBattleModule extends AnnotationModule {
}
