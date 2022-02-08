package ru.jengine.battlemodule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.battlemodule.modules.StandardFillingModule;
import ru.jengine.beancontainer.annotations.ImportModule;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportModule(StandardFillingModule.class)
@EnableBattleCore
public @interface EnableBattleCoreWithStandardFilling {
}
