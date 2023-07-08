package ru.jengine.utils.serviceclasses;

public interface ServiceLocator<T> {
    <S extends T> void setService(Class<S> serviceType, S service);
    <S extends T> S getService(Class<S> serviceType);
    boolean containsService(Class<? extends T> serviceType);
}
