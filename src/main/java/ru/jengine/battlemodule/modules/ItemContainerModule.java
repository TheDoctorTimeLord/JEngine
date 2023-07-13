package ru.jengine.battlemodule.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;

/**
 * Модуль, добавляющий отдельно всё, что касается сохранения данных о контейнерах или предметах
 */
@ContainerModule(contextName = Contexts.BATTLE_CONTEXT)
@PackageScan("ru.jengine.battlemodule.standardfilling.itemcontainers")
public class ItemContainerModule extends AnnotationModule {
}
