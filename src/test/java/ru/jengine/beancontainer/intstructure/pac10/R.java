package ru.jengine.beancontainer.intstructure.pac10;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.annotations.PreDestroy;

@Bean
public class R {
    private int specialMethodsCalledCounter;

    @PostConstruct
    private void postConstruct(S gotParameter) {
        specialMethodsCalledCounter++;

        if (gotParameter != null) {
            specialMethodsCalledCounter++;
        }
    }

    @PreDestroy
    private void preDestroy() {
        specialMethodsCalledCounter++;
    }

    public int getSpecialMethodsCalledCounter() {
        return specialMethodsCalledCounter;
    }
}
