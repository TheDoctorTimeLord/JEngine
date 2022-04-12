package ru.jengine.battlemodule.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.service.Constants.Contexts;

/**
 * Модуль, добавляющий отдельно всё, что касается сохранения данных о контейнерах или предметах
 */
@ContainerModule
@Context(Contexts.BATTLE_CONTEXT)
@PackageScan("ru.jengine.battlemodule.standardfilling.itemcontainers")
public class ItemContainerModule extends AnnotationModule {
}
