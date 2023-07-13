package ru.jengine.battlemodule.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;

/**
 * Модуль, сканирующий пакет стандартного наполнения боевой системы.
 */
@ContainerModule(contextName = Contexts.BATTLE_CONTEXT)
@PackageScan("ru.jengine.battlemodule.standardfilling")
public class StandardFillingModule extends AnnotationModule {
}
