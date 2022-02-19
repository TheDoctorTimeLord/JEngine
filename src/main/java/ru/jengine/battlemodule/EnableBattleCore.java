package ru.jengine.battlemodule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.battlemodule.modules.CoreBattleModule;
import ru.jengine.beancontainer.annotations.ImportModule;

/**
 * Подключает к проекту контекст, содержащий основное ядро пошаговой боевой системы (см. подробнее
 * {@link ru.jengine.battlemodule.core.BattleMaster BattleMaster}). Для подключения боевой системы — разместите эту
 * аннотацию над одним из своих модулей.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportModule(CoreBattleModule.class)
public @interface EnableBattleCore {
}
