package ru.jengine.beancontainer.intstructure.pac15;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.SharedBeansProvider;

import java.util.List;

@Bean
public class AA {
    private List<Available> availableShared;

    @SharedBeansProvider
    private void shareBeans(List<Available> availableShared) {
        this.availableShared = availableShared;
    }

    public List<Available> getAvailableShared() {
        return availableShared;
    }
}
