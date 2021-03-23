package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Configurators;
import ru.jengine.beancontainer.annotations.Initializer;

@Configurators
public class Bean2Configurator {
    @Initializer(forClass = Bean2.class)
    public void configuration(Bean2 bean) {
        bean.setF(true);
    }
}
