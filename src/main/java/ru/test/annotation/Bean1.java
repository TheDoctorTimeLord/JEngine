package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.annotations.RemoveAfterInitialize;

@Bean
@RemoveAfterInitialize
public class Bean1 {
    private boolean f = false;

    @PostConstruct
    public void postConstruct() {
        f = true;
    }

    public boolean isF() {
        return f;
    }
}
