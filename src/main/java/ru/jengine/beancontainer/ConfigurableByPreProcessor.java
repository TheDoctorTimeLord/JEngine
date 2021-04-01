package ru.jengine.beancontainer;

public interface ConfigurableByPreProcessor {
    boolean mustRemovedAfterPreProcess();
    void setMustRemovedAfterPreProcess(boolean flag);
}
