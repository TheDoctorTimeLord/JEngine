package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Inject;

@Bean
public class Bean3 {
    private final Bean1 bean1;
    private final Bean2 bean2;

    @Inject
    public Bean3(Bean1 bean1, Bean2 bean2) {
        this.bean1 = bean1;
        this.bean2 = bean2;
    }

    public boolean isF() {
        return bean1.isF() && bean2.isF();
    }
}
