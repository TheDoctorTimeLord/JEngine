package ru.jengine.beancontainer.intstructure.pac_11;

import java.util.ArrayList;
import java.util.List;

public class RemoveCounter {
    private final List<String> removedContexts = new ArrayList<>();

    public void addRemoveContextEntry(String contextName) {
        removedContexts.add(contextName);
    }

    public List<String> getRemovedContexts() {
        return removedContexts;
    }
}
