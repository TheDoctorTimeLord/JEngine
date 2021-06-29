package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Configurator;
import ru.jengine.beancontainer.annotations.Configurators;

@Configurators
public class Bean2Configurator {
    @Configurator(forClass = Bean2.class)
    public void configuration(Bean2 bean) {
        bean.setF(true);
    }
}
