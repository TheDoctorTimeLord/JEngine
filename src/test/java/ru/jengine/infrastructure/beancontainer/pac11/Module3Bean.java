package ru.jengine.infrastructure.beancontainer.pac11;

import ru.jengine.beancontainer.annotations.PreDestroy;

public class Module3Bean {
    private final RemoveCounter removeCounter;

    public Module3Bean(RemoveCounter removeCounter) {
        this.removeCounter = removeCounter;
    }

    @PreDestroy
    public void preRemove() {
        removeCounter.addRemoveContextEntry("module3");
    }
}
