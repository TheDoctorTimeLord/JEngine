package ru.jengine.beancontainer.intstructure.pac11;

import ru.jengine.beancontainer.annotations.PreDestroy;

public class Module1Bean {
    private final RemoveCounter removeCounter;

    public Module1Bean(RemoveCounter removeCounter) {
        this.removeCounter = removeCounter;
    }

    @PreDestroy
    public void preRemove() {
        removeCounter.addRemoveContextEntry("module1");
    }
}
