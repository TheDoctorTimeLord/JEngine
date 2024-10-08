package ru.jengine.infrastructure.beancontainer.pac11;

import ru.jengine.beancontainer.annotations.PreDestroy;

public class Module2Bean {
    private final RemoveCounter removeCounter;

    public Module2Bean(RemoveCounter removeCounter) {
        this.removeCounter = removeCounter;
    }

    @PreDestroy
    public void preRemove() {
        removeCounter.addRemoveContextEntry("module2");
    }
}
