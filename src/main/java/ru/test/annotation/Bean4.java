package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Inject;

@Bean
public class Bean4 {
    private final Bean3 bean3;
    private boolean f = false;

    public Bean4(Bean3 bean3) {
        this.bean3 = bean3;
    }

    @Inject
    private void init(Bean1 bean1) {
        f = bean1.isF();
    }

    public boolean result() {
        return bean3.isF() && f;
    }
}
