package ru.jengine.battlemodule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.battlemodule.modules.AttributesHandlingModule;
import ru.jengine.beancontainer.annotations.ImportModule;

/**
 * Подключает к проекту стандартный модуль
 * {@link ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandlerManager обработки
 * атрибутов}.<br>
 * ВАЖНО: аннотация {@link EnableBattleCoreWithStandardFilling} позволяет использовать ВСЁ стандартное
 * наполнение, ВКЛЮЧАЯ то, что будет добавлено текущей аннотацией. Если вы используете аннотацию
 * {@link EnableBattleCoreWithStandardFilling}, то добавлять текущую аннотацию НЕ НУЖНО
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportModule(AttributesHandlingModule.class)
public @interface EnableBattleAttributeHandling {
}
