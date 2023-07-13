package ru.jengine.beancontainer.intstructure.pac5;

import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class D {
    private boolean test1 = false;
    private boolean test2 = false;

    public boolean isTest2() {
        return test2;
    }

    public boolean isGlobalTest() {
        return test1 && test2;
    }
}
