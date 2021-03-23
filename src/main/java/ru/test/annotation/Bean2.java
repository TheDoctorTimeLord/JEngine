package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.beancontainer.annotations.PostConstruct;

@Bean
public class Bean2 {
    private boolean f = false;
    private final Bean1 bean1;

    @Inject
    public Bean2(Bean1 bean1) {
        this.bean1 = bean1;
    }

    @PostConstruct
    private void postConstruct() {
        f = f && bean1.isF();
    }

    public void setF(boolean f) {
        this.f = f;
    }

    public boolean isF() {
        return f;
    }
}
