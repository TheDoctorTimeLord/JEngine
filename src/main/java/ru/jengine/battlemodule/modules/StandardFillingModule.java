package ru.jengine.battlemodule.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.service.Constants.Contexts;

/**
 * Модуль, сканирующий пакет стандартного наполнения боевой системы.
 */
@ContainerModule
@Context(Contexts.BATTLE_CONTEXT)
@PackageScan("ru.jengine.battlemodule.standardfilling")
public class StandardFillingModule extends AnnotationModule {
}
